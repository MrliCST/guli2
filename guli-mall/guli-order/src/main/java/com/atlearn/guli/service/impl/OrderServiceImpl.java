package com.atlearn.guli.service.impl;

import com.atlearn.guli.RemoteMemberService;
import com.atlearn.guli.RemoteProductService;
import com.atlearn.guli.RemoteShopCartService;
import com.atlearn.guli.RemoteWareService;
import com.atlearn.guli.constant.OrderConstant;
import com.atlearn.guli.core.UserInfoContext;
import com.atlearn.guli.domain.OmsOrder;
import com.atlearn.guli.domain.OmsOrderItem;
import com.atlearn.guli.domain.bo.RmeWareSkuLockBo;
import com.atlearn.guli.domain.bo.SubmitOrderBo;
import com.atlearn.guli.domain.vo.OrderConfirmVo;
import com.atlearn.guli.domain.vo.RmeCartItemVo;
import com.atlearn.guli.domain.vo.RmeMemberReceiveAddressVO;
import com.atlearn.guli.domain.vo.RmeSkuInfoVo;
import com.atlearn.guli.domain.vo.RmeWareStockLockResultVo;
import com.atlearn.guli.enums.OrderStatusEnum;
import com.atlearn.guli.exception.BusinessException;
import com.atlearn.guli.exception.ErrorCodeEnum;
import com.atlearn.guli.mapper.OmsOrderItemMapper;
import com.atlearn.guli.mapper.OmsOrderMapper;
import com.atlearn.guli.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.common.mybatis.utils.IdGeneratorUtil;
import org.dromara.common.redis.utils.RedisUtils;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * 订单服务实现
 *
 * @author guli
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements IOrderService {

    @DubboReference
    private RemoteMemberService remoteMemberService;

    @DubboReference
    private RemoteShopCartService remoteShopCartService;

    @DubboReference
    private RemoteWareService remoteWareService;

    @DubboReference
    private RemoteProductService remoteProductService;

    private final OmsOrderMapper omsOrderMapper;
    private final OmsOrderItemMapper omsOrderItemMapper;

    @Autowired
    @Qualifier("executor")
    private ThreadPoolExecutor executor;

    /** 订单确认页 Redis key 前缀 */
    private static final String CONFIRM_KEY_PREFIX = "gulimall:confirm:";

    /**
     * 防重 Token 校验+删除 Lua 脚本
     * KEYS[1]: token key
     * ARGV[1]: token 值
     * 返回 1=校验通过且已删除, 0=校验失败
     */
    private static final String CHECK_DELETE_TOKEN_LUA =
        "if redis.call('GET', KEYS[1]) == ARGV[1] then " +
        "    return redis.call('DEL', KEYS[1]) " +
        "else " +
        "    return 0 " +
        "end";

    /**
     * 展示订单，以供用户确认
     */
    @Override
    public OrderConfirmVo confirmOrder(List<Long> skuIds) {
        Long memberId = UserInfoContext.getUserId();

        // 缓存用户勾选的 skuId 列表，供后续下单使用
        RedisUtils.setCacheObject(CONFIRM_KEY_PREFIX + memberId, skuIds);

        // 并行获取收货地址和购物车列表
        CompletableFuture<List<RmeMemberReceiveAddressVO>> addressFuture = CompletableFuture.supplyAsync(
            () -> remoteMemberService.getReceiveAddressList(memberId), executor
        ).exceptionally(throwable -> {
            log.error("获取收货地址失败", throwable);
            throw new BusinessException(ErrorCodeEnum.ORDER_CONFIRM_FAILED);
        });

        CompletableFuture<List<RmeCartItemVo>> cartFuture = fetchCartItemsWithStock(skuIds);
        CompletableFuture.allOf(addressFuture, cartFuture).join();  // 等待异步线程均到达连接点

        // 从期货中取出结果
        List<RmeMemberReceiveAddressVO> addressList = addressFuture.join();
        List<RmeCartItemVo> selectedCartItems = cartFuture.join();

        // 计算金额
        BigDecimal productAmount = calculateProductAmount(selectedCartItems);
        BigDecimal shippingFee = remoteWareService.getShippingFee("河北地质大学");
        BigDecimal totalAmount = productAmount.add(shippingFee);

        // 生成防重令牌并存入 Redis
        String orderToken = IdGeneratorUtil.nextUUID();
        String tokenKey = OrderConstant.ORDER_TOKEN_PREFIX + memberId;
        RedisUtils.setCacheObject(tokenKey, orderToken, OrderConstant.ORDER_TOKEN_TTL);

        return OrderConfirmVo.builder()
            .memberReceiveAddressList(addressList)
            .cartItemList(selectedCartItems)
            .totalAmount(totalAmount)
            .payAmount(totalAmount)
            .orderToken(orderToken)
            .build();
    }

    /**
     * 提交订单
     * 链路：防重Token校验 → 获取购物车+地址 → 价格校验（实时价格对比） → 生成订单号 → 保存订单+订单项 → 远程锁库存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitOrder(SubmitOrderBo bo) {
        Long memberId = UserInfoContext.getUserId();

        // 1. 防重 Token 校验（Lua 脚本原子校验+删除）
        if (!checkAndDeleteToken(memberId, bo.getOrderToken())) {
            throw new BusinessException(ErrorCodeEnum.ORDER_TOKEN_INVALID);
        }

        // 2. 从 Redis 取确认页缓存的 skuIds
        List<Long> skuIds = RedisUtils.getCacheObject(CONFIRM_KEY_PREFIX + memberId);
        if (skuIds == null || skuIds.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.CART_EMPTY);
        }

        // 3. 并行获取收货地址和购物车
        CompletableFuture<RmeMemberReceiveAddressVO> addressFuture = CompletableFuture.supplyAsync(
            () -> remoteMemberService.getReceiveAddressById(bo.getMemberReceiveAddressId()), executor
        ).exceptionally(throwable -> {
            log.error("获取收货地址失败", throwable);
            throw new BusinessException(ErrorCodeEnum.ADDRESS_NOT_FOUND);
        });

        CompletableFuture<List<RmeCartItemVo>> cartFuture = fetchCartItemsWithStock(skuIds);
        CompletableFuture.allOf(addressFuture, cartFuture).join(); //阻塞等待 A、B 两个异步任务全部执行完毕

        RmeMemberReceiveAddressVO address = addressFuture.join();
        if (address == null) {
            throw new BusinessException(ErrorCodeEnum.ADDRESS_NOT_FOUND);
        }
        List<RmeCartItemVo> selectedCartItems = cartFuture.join().stream()  //只保留有库存的商品
            .filter(RmeCartItemVo::getHasStock)
            .collect(Collectors.toList());
        if (selectedCartItems.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.CART_EMPTY);
        }

        // 4. 价格校验：获取实时价格，与购物车价格对比，差值 < 0.01 视为通过
        List<Long> hasStockSkuIds = selectedCartItems.stream()
            .map(RmeCartItemVo::getSkuId)
            .collect(Collectors.toList());
        Map<Long, RmeSkuInfoVo> skuIdToInfoMap = remoteProductService.getSkuInfoMapBySkuIds(hasStockSkuIds);

        BigDecimal cartTotal = calculateCartTotal(selectedCartItems);
        BigDecimal realTotal = calculateRealTotal(selectedCartItems, skuIdToInfoMap);
        if (cartTotal.subtract(realTotal).abs()
            .compareTo(OrderConstant.PRICE_DIFF_THRESHOLD) > 0) {
            log.warn("价格变动校验失败，购物车总价={}, 实时总价={}", cartTotal, realTotal);
            throw new BusinessException(ErrorCodeEnum.PRICE_CHANGED);
        }

        // 5. 生成订单号（雪花算法）
        String orderSn = String.valueOf(IdGeneratorUtil.nextLongId());

        // 6. 计算运费 + 应付金额
        BigDecimal shippingFee = remoteWareService.getShippingFee("河北地质大学");
        BigDecimal totalAmount = realTotal.add(shippingFee);

        // 7. 构建 OmsOrder(订单主表) 并插入
        OmsOrder order = OmsOrder.builder()
            .memberId(memberId)
            .orderSn(orderSn)
            .totalAmount(realTotal)
            .payAmount(totalAmount)
            .freightAmount(shippingFee)
            // 优惠折扣信息
            .couponId(0L)
            .useIntegration(0)
            .promotionAmount(BigDecimal.ZERO)
            .integrationAmount(BigDecimal.ZERO)
            .couponAmount(BigDecimal.ZERO)
            .discountAmount(BigDecimal.ZERO)
            // 支付方式
            .payType(bo.getPayType())
            .sourceType(OrderConstant.SOURCE_TYPE_APP)
            // 物流相关
            .deliveryCompany("")
            .deliverySn("")
            // 个人积分
            .integration(0)
            .growth(0)
            // 发票相关
            .billType(0)
            .billHeader("")
            .billContent("")
            .billReceiverPhone("")
            .billReceiverEmail("")
            // 收货人信息
            .receiverName(address.getName())
            .receiverPhone(address.getPhone())
            .receiverPostCode(address.getPostCode())
            .receiverProvince(address.getProvince())
            .receiverCity(address.getCity())
            .receiverRegion(address.getRegion())
            .receiverDetailAddress(address.getDetailAddress())
            .note(bo.getNote())
            // 状态相关
            .status(OrderStatusEnum.UNPAID.getCode())
            .confirmStatus(OrderConstant.CONFIRM_STATUS_NORMAL)
            .deleteStatus(OrderConstant.DELETE_STATUS_NORMAL)
            .autoConfirmDay(7)
            .modifyTime(new Date())
            .build();
        omsOrderMapper.insert(order);

        // 8. 构建订单项并插入
        List<OmsOrderItem> orderItems = selectedCartItems.stream()
            .map(cartItem -> {
                RmeSkuInfoVo skuInfo = skuIdToInfoMap.get(cartItem.getSkuId());
                return OmsOrderItem.builder()
                    .orderId(order.getId())
                    .orderSn(orderSn)
                    .spuId(skuInfo != null ? skuInfo.getSpuId() : null)
                    .spuName(skuInfo != null ? skuInfo.getSkuName() : null)
                    .spuPic(null)
                    .spuBrandId(skuInfo != null ? skuInfo.getBrandId() : null)
                    .categoryId(skuInfo != null ? skuInfo.getCatalogId() : null)
                    .skuId(cartItem.getSkuId())
                    .skuName(cartItem.getTitle())
                    .skuPic(cartItem.getDefaultImage())
                    // 使用实时价格
                    .skuPrice(skuInfo != null ? skuInfo.getPrice() : cartItem.getPrice())
                    .skuQuantity(cartItem.getCount())
                    .skuAttrsVals(cartItem.getSaleAttr().stream()
                        .map(attr -> attr.getAttrName() + ":" + attr.getAttrValue())
                        .collect(Collectors.joining(";")))
                    .promotionAmount(BigDecimal.ZERO)
                    .couponAmount(BigDecimal.ZERO)
                    .integrationAmount(BigDecimal.ZERO)
                    .realAmount(cartItem.getTotalPrice())
                    .giftIntegration(0)
                    .giftGrowth(0)
                    .build();
            }).collect(Collectors.toList());
        for (OmsOrderItem item : orderItems) {
            omsOrderItemMapper.insert(item);
        }

        // 9. 远程调用仓储服务锁定库存
        List<RmeWareSkuLockBo.LockItem> lockItems = selectedCartItems.stream()
            .map(item -> RmeWareSkuLockBo.LockItem.builder()
                .skuId(item.getSkuId())
                .skuName(item.getTitle())
                .skuNum(item.getCount())
                .build())
            .collect(Collectors.toList());
        RmeWareSkuLockBo lockBo = RmeWareSkuLockBo.builder()
            .orderSn(orderSn)
            .lockItems(lockItems)
            .build();
        RmeWareStockLockResultVo lockResult = remoteWareService.orderLockStock(lockBo);
        if (!lockResult.getSuccess()) {
            log.error("订单[{}]库存锁定失败：{}", orderSn, lockResult.getMessage());
            throw new BusinessException(ErrorCodeEnum.STOCK_LOCK_FAILED);
        }

        // 10. 清除确认页缓存
        RedisUtils.deleteObject(CONFIRM_KEY_PREFIX + memberId);

        log.info("订单提交成功，orderSn={}, 商品数={}", orderSn, orderItems.size());
        return orderSn;
    }

    /**
     * 校验并删除防重 Token（Lua 脚本保证原子性）
     *
     * @param memberId   会员ID
     * @param orderToken 令牌
     * @return true-校验通过且已删除
     */
    private boolean checkAndDeleteToken(Long memberId, String orderToken) {
        if (orderToken == null || orderToken.isEmpty()) {
            return false;
        }
        String key = OrderConstant.ORDER_TOKEN_PREFIX + memberId;
        RedissonClient client = RedisUtils.getClient();
        RScript script = client.getScript();
        Long result = script.eval(
            RScript.Mode.READ_WRITE,
            CHECK_DELETE_TOKEN_LUA,
            RScript.ReturnType.INTEGER,
            Collections.singletonList(key),
            orderToken
        );
        return result != null && result == 1L;
    }

    /**
     * 计算购物车总价（按购物车中的价格计算）
     */
    private BigDecimal calculateCartTotal(List<RmeCartItemVo> cartItems) {
        return cartItems.stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getCount())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 计算实时价格总价（按商品服务的实时价格计算）
     */
    private BigDecimal calculateRealTotal(List<RmeCartItemVo> cartItems,
                                          Map<Long, RmeSkuInfoVo> skuInfoMap) {
        return cartItems.stream()
            .map(item -> {
                RmeSkuInfoVo skuInfo = skuInfoMap.get(item.getSkuId());
                BigDecimal price = skuInfo != null ? skuInfo.getPrice() : item.getPrice();
                return price.multiply(BigDecimal.valueOf(item.getCount()));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 获取购物车项并设置库存状态（不剔除缺货商品，由调用方决定是否过滤）
     */
    private CompletableFuture<List<RmeCartItemVo>> fetchCartItemsWithStock(List<Long> skuIds) {
        return CompletableFuture.supplyAsync(() -> {
            //1. 获取购物车列表
            List<RmeCartItemVo> cartItemList = remoteShopCartService.getCartItemList();
            //2. 获取一批sku的库存信息
            Map<Long, Long> skuAvailableStock = remoteWareService.getSkuAvailableStock(skuIds);
            //3. 转为不可变Set
            Set<Long> skuIdSet = Set.copyOf(skuIds);
            //4. 遍历购物车列表，给勾选的购物车项设置库存状态
            return cartItemList.stream()
                .filter(cartItem -> skuIdSet.contains(cartItem.getSkuId()))
                .map(cartItem -> {
                    cartItem.setHasStock(skuAvailableStock.getOrDefault(cartItem.getSkuId(), 0L) > 0);
                    return cartItem;
                })
                .collect(Collectors.toList());
        }, executor).exceptionally(throwable -> {
            log.error("获取购物车或库存信息失败", throwable);
            throw new BusinessException(ErrorCodeEnum.ORDER_CONFIRM_FAILED);
        });
    }

    /**
     * 计算商品总金额（只含有货的商品）
     */
    @SuppressWarnings("null")
    private BigDecimal calculateProductAmount(List<RmeCartItemVo> cartItems) {
        return cartItems.stream()
            .filter(RmeCartItemVo::getHasStock)
            .map(cartItem -> cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getCount())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);  // 归约
    }
}
