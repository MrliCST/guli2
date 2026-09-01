package com.atlearn.guli.service.impl;

import com.atlearn.guli.RemoteMemberService;
import com.atlearn.guli.RemoteProductService;
import com.atlearn.guli.RemoteShopCartService;
import com.atlearn.guli.RemoteWareService;
import com.atlearn.guli.constant.OrderConstant;
import com.atlearn.guli.core.UserInfoContext;
import com.atlearn.guli.domain.OmsOrder;
import com.atlearn.guli.domain.OmsOrderItem;
import com.atlearn.guli.domain.bo.RmeLockWareBo;
import com.atlearn.guli.domain.bo.RmeOrderInfoBo;
import com.atlearn.guli.domain.bo.SubmitOrderBo;
import com.atlearn.guli.domain.vo.OrderConfirmVo;
import com.atlearn.guli.domain.vo.RmeCartItemVo;
import com.atlearn.guli.domain.vo.RmeMemberReceiveAddressVO;
import com.atlearn.guli.domain.vo.RmeSkuInfoVo;
import com.atlearn.guli.domain.vo.RmeWareSkuVo;
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
import java.util.Comparator;
import java.util.Date;
import java.util.ArrayList;
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
     * 展示订单，以供用户确认
     */
    @Override
    public OrderConfirmVo confirmOrder(List<Long> skuIds) {
        Long memberId = UserInfoContext.getUserId();

        // 1. 缓存用户勾选的 skuId 列表，供后续下单使用
        RedisUtils.setCacheObject(CONFIRM_KEY_PREFIX + memberId, skuIds);

        // 2. 并行获取收货地址和购物车列表
        CompletableFuture<List<RmeMemberReceiveAddressVO>> addressFuture = CompletableFuture.supplyAsync(
            () -> remoteMemberService.getReceiveAddressList(memberId), executor
        ).exceptionally(throwable -> {
            log.error("获取收货地址失败", throwable);
            throw new BusinessException(ErrorCodeEnum.ORDER_CONFIRM_FAILED);
        });

        CompletableFuture<List<RmeCartItemVo>> cartFuture = CompletableFuture.supplyAsync(() -> {
            return remoteShopCartService.getCartItemList();
        }, executor).exceptionally(throwable -> {
            log.error("获取购物车或库存信息失败", throwable);
            throw new BusinessException(ErrorCodeEnum.ORDER_CONFIRM_FAILED);
        });
        CompletableFuture.allOf(addressFuture, cartFuture).join();  // 等待异步线程均到达连接点

        // 从期货中取出结果
        List<RmeMemberReceiveAddressVO> addressList = addressFuture.join();
        if (addressList == null || addressList.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.ADDRESS_NOT_FOUND);
        }

        List<RmeCartItemVo> cartItems = cartFuture.join();
        if (cartItems == null || cartItems.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.CART_EMPTY);
        }

        // 3. 验库存 + 过滤勾选商品并标记库存状态
        List<RmeCartItemVo> selectedCartItems = filterAndMarkStock(skuIds, cartItems);

        // 4. 计算金额
        BigDecimal productAmount = calculateProductAmount(selectedCartItems);
        BigDecimal shippingFee = remoteWareService.getShippingFee("河北地质大学");
        BigDecimal totalAmount = productAmount.add(shippingFee);

        return OrderConfirmVo.builder()
            .memberReceiveAddressList(addressList)
            .cartItemList(selectedCartItems)
            .totalAmount(totalAmount)
            .payAmount(totalAmount)
            .build();
    }

    /**
     * 提交订单
     * 链路：防重Token校验 → 获取购物车+地址 → 验库存 → 生成订单号+锁库存 → 价格校验 → 保存订单+订单项
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("null")
    public String submitOrder(SubmitOrderBo bo) {
        Long memberId = UserInfoContext.getUserId();

        // ===============  1. 从 Redis 取确认页缓存的 skuIds  ===============
        List<Long> skuIds = RedisUtils.getCacheObject(CONFIRM_KEY_PREFIX + memberId);
        if (skuIds == null || skuIds.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.CART_EMPTY);
        }

        // ===============  2. 并行获取收货地址和购物车  ===============
        CompletableFuture<RmeMemberReceiveAddressVO> addressFuture = CompletableFuture.supplyAsync(
            () -> remoteMemberService.getReceiveAddressById(bo.getMemberReceiveAddressId()), executor
        ).exceptionally(throwable -> {
            log.error("获取收货地址失败", throwable);
            throw new BusinessException(ErrorCodeEnum.ADDRESS_NOT_FOUND);
        });

        CompletableFuture<List<RmeCartItemVo>> cartFuture = CompletableFuture.supplyAsync(
            () -> remoteShopCartService.getCartItemList(), executor
        ).exceptionally(throwable -> {
            log.error("获取购物车失败", throwable);
            throw new BusinessException(ErrorCodeEnum.ORDER_CONFIRM_FAILED);
        });
        CompletableFuture.allOf(addressFuture, cartFuture).join(); // 阻塞等待 A、B 两个异步任务全部执行完毕

        // 取出期货中的结果
        RmeMemberReceiveAddressVO address = addressFuture.join();
        if (address == null) throw new BusinessException(ErrorCodeEnum.ADDRESS_NOT_FOUND);

        List<RmeCartItemVo> cartItems = cartFuture.join();
        if (cartItems == null || cartItems.isEmpty()) throw new BusinessException(ErrorCodeEnum.CART_EMPTY);
        
        // ===============  3. 验库存：库存是否足够  ===============
        List<RmeCartItemVo> selectedCartItems = filterAndMarkStock(skuIds, cartItems); // 过滤并标记库存是否充足
        if (selectedCartItems == null || selectedCartItems.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.CART_EMPTY);  
        }

        // ===============  4. 生成订单号 + 锁定库存  ===============
        // 每个 sku 需要锁定的数量
        Map<Long, Integer> skuIdToNeedLockedNumMap = selectedCartItems.stream()
            .collect(Collectors.toMap(RmeCartItemVo::getSkuId, RmeCartItemVo::getCount));

        // 各仓库库存信息明细
        List<RmeWareSkuVo> flatWareSkuInfo = remoteWareService.getWareSkuListBySkuIds(skuIds);
        Map<Long, List<RmeWareSkuVo>> WareSkuInfoGroup = flatWareSkuInfo.stream()
            .sorted(Comparator.comparing(RmeWareSkuVo::getAvailableStock).reversed()) // 降序
            .collect(Collectors.groupingBy(RmeWareSkuVo::getSkuId)); // 分组

        // 分配仓库，构建锁定信息
        List<RmeLockWareBo> lockWareBoList = buildLockWareBoList(skuIdToNeedLockedNumMap, WareSkuInfoGroup);

        // 生成订单号（提前生成，用于库存工作单关联）
        String orderSn = String.valueOf(IdGeneratorUtil.nextLongId());

        // 远程锁定库存（携带订单冗余信息，服务端任意一条失败即抛异常回滚）
        RmeOrderInfoBo orderInfo = RmeOrderInfoBo.builder()
            .orderSn(orderSn)
            .consignee(address.getName())
            .consigneeTel(address.getPhone())
            .deliveryAddress(address.getProvince() + address.getCity() + address.getRegion() + address.getDetailAddress())
            .paymentWay(bo.getPayType())
            .orderBody("商品数:" + selectedCartItems.size())
            .build();
        boolean lockSuccess = remoteWareService.lockWareSkuBatch(orderInfo, lockWareBoList);
        if (!lockSuccess) {
            throw new BusinessException(ErrorCodeEnum.ORDER_CONFIRM_FAILED);
        }

        // ===============  5. 价格校验  ===============
        Map<Long, RmeSkuInfoVo> skuIdToInfoMap = remoteProductService.getSkuInfoMapBySkuIds(skuIds);

        // 选中购物车商品，更新设置为最新价格
        List<RmeCartItemVo> currentPriceCartItemListWithSelected = selectedCartItems.stream().map(cartItem -> {
            Long skuId = cartItem.getSkuId();
            BigDecimal currentPrice = skuIdToInfoMap.get(skuId).getPrice();  // 获取实时价格
            cartItem.setPrice(currentPrice);
            return cartItem;
        }).collect(Collectors.toList());

        // 开始验价
        BigDecimal referenceAmount = bo.getPayRefenceAmount(); // 参考金额
        BigDecimal productTotalPrice = calculateProductAmount(currentPriceCartItemListWithSelected);   //商品费用
        BigDecimal shippingFee = remoteWareService.getShippingFee("河北地质大学"); // 运费
        BigDecimal totalAmount = productTotalPrice.add(shippingFee);  // 验价金额
        if (referenceAmount.subtract(totalAmount).abs()
            .compareTo(OrderConstant.PRICE_DIFF_THRESHOLD) > 0) {
            log.warn("价格变动校验失败，购物车总价={}, 实时总价={}", referenceAmount, totalAmount);
            throw new BusinessException(ErrorCodeEnum.PRICE_CHANGED);  // ===> 金额改变失败
        }

        // ===============  6. 优惠打折 (不做)  ===============
        BigDecimal payAmount = totalAmount;

        // ===============  7. 构建 OmsOrder(订单主表) 并插入  ===============
        OmsOrder order = OmsOrder.builder()
            .memberId(memberId)
            .orderSn(orderSn)
            .totalAmount(totalAmount)
            .payAmount(payAmount)
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

        // ===============  8. 构建订单项并插入  ===============
        List<OmsOrderItem> orderItems = selectedCartItems.stream()
            .map(cartItem -> {
                RmeSkuInfoVo skuInfo = skuIdToInfoMap.get(cartItem.getSkuId());

                return OmsOrderItem.builder()
                    .orderId(order.getId())
                    .orderSn(orderSn)
                    .spuId(skuInfo.getSpuId())
                    .spuName(skuInfo.getSkuName())
                    .spuPic(null)
                    .spuBrandId(skuInfo.getBrandId())
                    .categoryId(skuInfo.getCatalogId())
                    .skuId(cartItem.getSkuId())
                    .skuName(cartItem.getTitle())
                    .skuPic(cartItem.getDefaultImage())
                    // 使用实时价格
                    .skuPrice(skuInfo.getPrice())
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
        omsOrderItemMapper.insertBatch(orderItems);

        // ===============  10. 清除确认页缓存  ===============
        RedisUtils.deleteObject(CONFIRM_KEY_PREFIX + memberId);

        log.info("订单提交成功，orderSn={}, 商品数={}", orderSn, orderItems.size());
        return orderSn;
    }

    /**
     * 查询库存并过滤勾选商品，标记库存状态
     */
    private List<RmeCartItemVo> filterAndMarkStock(List<Long> skuIds, List<RmeCartItemVo> cartItems) {
        // sku的所有可用库存
        Map<Long, Long> skuAvailableStock = remoteWareService.getSkuAvailableStock(skuIds);
        Set<Long> skuIdSet = Set.copyOf(skuIds);
        return cartItems.stream()
            .filter(cartItem -> skuIdSet.contains(cartItem.getSkuId())) // 过滤未勾选的
            .map(cartItem -> {
                Long skuId = cartItem.getSkuId();
                Long availableStock = skuAvailableStock.getOrDefault(skuId, 0L);
                cartItem.setHasStock(cartItem.getCount() <= availableStock);
                return cartItem;
            })
            .collect(Collectors.toList());
    }

    /**
     * 计算商品总金额（只含有货的商品）
     */
    @SuppressWarnings("null")
    private BigDecimal calculateProductAmount(List<RmeCartItemVo> cartItems) {
        return cartItems.stream()
            .filter(RmeCartItemVo::getHasStock)  // 跳过无库存商品
            .map(cartItem -> cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getCount())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);  // 归约
    }

    /**
     * 仓库分配算法：按可用库存降序，优先从库存最多的仓库锁定，不够再找下一个
     *
     * @param skuIdToNeedLockedNumMap 每个 sku 需要锁定的数量
     * @param wareSkuInfoGroup        各 sku 对应的仓库库存列表（已按可用库存降序）
     * @return 锁定信息列表
     */
    private List<RmeLockWareBo> buildLockWareBoList(
            Map<Long, Integer> skuIdToNeedLockedNumMap,
            Map<Long, List<RmeWareSkuVo>> wareSkuInfoGroup) {

        List<RmeLockWareBo> result = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : skuIdToNeedLockedNumMap.entrySet()) {
            Long skuId = entry.getKey();
            int needLockNum = entry.getValue();

            List<RmeWareSkuVo> wareList = wareSkuInfoGroup.get(skuId);
            if (wareList == null || wareList.isEmpty()) {
                log.warn("skuId={} 没有可用的仓库库存", skuId);
                continue;
            }

            List<RmeLockWareBo.WareDistribute> distributes = new ArrayList<>();
            int remaining = needLockNum;

            for (RmeWareSkuVo ware : wareList) {
                long available = ware.getAvailableStock();
                int lockNum = remaining <= available ? remaining : (int) available;
                distributes.add(RmeLockWareBo.WareDistribute.builder()
                    .wareId(ware.getWareId())
                    .lockNum(lockNum)
                    .build());
                remaining -= lockNum;
                if (remaining <= 0) break;
            }

            result.add(RmeLockWareBo.builder()
                .skuId(skuId)
                .wareDistribute(distributes)
                .build());
        }

        return result;
    }

    /**
     * 废弃的防重校验方法
     * 
     * 防重 Token 校验+删除 Lua 脚本
     * KEYS[1]: token key
     * ARGV[1]: token 值
     * 返回 1=校验通过且已删除, 0=校验失败
     */
    @Deprecated
    private boolean checkAndDeleteToken(String AntiReplayToken) {
        /**
         * 原理：确认订单时，防重令牌发到前端
         * 提交时携带，后端验证后删除。
         * 
         * 多次提交时，只有第一个才能通过校验，后面的都会失败。
         */
        Long memberId = UserInfoContext.getUserId();
        String key = OrderConstant.ORDER_TOKEN_PREFIX + memberId;

        RedissonClient client = RedisUtils.getClient();
        RScript script = client.getScript();
        
        String luaScript = """
            if redis.call('GET', KEYS[1]) == ARGV[1] then
                return redis.call('DEL', KEYS[1])
            else 
                return 0
            end;
        """;
        
        Long result = script.eval(
            RScript.Mode.READ_WRITE,
            luaScript,
            RScript.ReturnType.INTEGER,
            Collections.singletonList(key),
            AntiReplayToken
        );
        return result != null && result == 1L;
    }
}
