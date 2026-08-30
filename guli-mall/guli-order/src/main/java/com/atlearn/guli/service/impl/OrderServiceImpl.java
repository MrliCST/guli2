package com.atlearn.guli.service.impl;

import com.atlearn.guli.RemoteMemberService;
import com.atlearn.guli.RemoteProductService;
import com.atlearn.guli.RemoteShopCartService;
import com.atlearn.guli.RemoteWareService;
import com.atlearn.guli.core.UserInfoContext;
import com.atlearn.guli.domain.OmsOrder;
import com.atlearn.guli.domain.OmsOrderItem;
import com.atlearn.guli.domain.bo.SubmitOrderBo;
import com.atlearn.guli.domain.vo.OrderConfirmVo;
import com.atlearn.guli.domain.vo.RmeCartItemVo;
import com.atlearn.guli.domain.vo.RmeMemberReceiveAddressVO;
import com.atlearn.guli.domain.vo.RmeSkuInfoVo;
import com.atlearn.guli.exception.BusinessException;
import com.atlearn.guli.exception.ErrorCodeEnum;
import com.atlearn.guli.mapper.OmsOrderItemMapper;
import com.atlearn.guli.mapper.OmsOrderMapper;
import com.atlearn.guli.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.common.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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

        return OrderConfirmVo.builder()
            .memberReceiveAddressList(addressList)
            .cartItemList(selectedCartItems)
            .totalAmount(totalAmount)
            .payAmount(totalAmount)
            .build();
    }

    /**
     * 提交订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("null")
    public String submitOrder(SubmitOrderBo bo) {
        Long memberId = UserInfoContext.getUserId();

        // 1. 从 Redis 取确认页缓存的 skuIds
        List<Long> skuIds = RedisUtils.getCacheObject(CONFIRM_KEY_PREFIX + memberId);
        if (skuIds == null || skuIds.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.ORDER_CONFIRM_FAILED);
        }

        // 2. 并行获取收货地址和购物车
        CompletableFuture<RmeMemberReceiveAddressVO> addressFuture = CompletableFuture.supplyAsync(
            () -> remoteMemberService.getReceiveAddressById(bo.getMemberReceiveAddressId()), executor
        ).exceptionally(throwable -> {
            log.error("获取收货地址失败", throwable);
            throw new BusinessException(ErrorCodeEnum.ORDER_CONFIRM_FAILED);
        });

        CompletableFuture<List<RmeCartItemVo>> cartFuture = fetchCartItemsWithStock(skuIds);
        CompletableFuture.allOf(addressFuture, cartFuture).join();  // 等待异步线程均到达连接点

        // 取出期货
        RmeMemberReceiveAddressVO address = addressFuture.join();
        if (address == null)  throw new BusinessException(ErrorCodeEnum.DATA_NOT_FOUND);
        List<RmeCartItemVo> selectedCartItems = cartFuture.join().stream()
            .filter(RmeCartItemVo::getHasStock)
            .collect(Collectors.toList());

        // 3. 开始验价
        BigDecimal productAmount = calculateProductAmount(selectedCartItems);
        BigDecimal shippingFee = remoteWareService.getShippingFee("河北地质大学");
        BigDecimal totalAmount = productAmount.add(shippingFee);
        if (totalAmount.compareTo(bo.getPayAmount()) != 0) {
            log.error("订单验价失败，后端={}, 前端={}", totalAmount, bo.getPayAmount());
            throw new BusinessException(ErrorCodeEnum.ORDER_CONFIRM_FAILED);
        }

        // 4. 生成订单号
        String orderSn = UUID.randomUUID().toString().replace("-", "");

        // 5. 构建 OmsOrder 并插入
        OmsOrder order = OmsOrder.builder()
            .memberId(memberId)
            .orderSn(orderSn)
            .totalAmount(totalAmount)
            .payAmount(bo.getPayAmount())
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
            .sourceType(0)
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
            .status(0)
            .confirmStatus(0)
            .deleteStatus(0)
            .autoConfirmDay(7)
            .modifyTime(new Date())
            .build();
        omsOrderMapper.insert(order);

        // 6. 构建 OmsOrderItem 并批量插入
        List<Long> hasStockSkuIds = selectedCartItems.stream().map(cartItem -> {
            return cartItem.getSkuId();
        }).collect(Collectors.toList());
        Map<Long,RmeSkuInfoVo> skuIdToInfoMap = remoteProductService.getSkuInfoMapBySkuIds(hasStockSkuIds);
        
        List<OmsOrderItem> orderItems = selectedCartItems.stream()
            .map(cartItem -> { 
                RmeSkuInfoVo skuInfo = skuIdToInfoMap.get(cartItem.getSkuId());
                
                return OmsOrderItem.builder()
                    .orderId(order.getId())
                    .orderSn(orderSn)
                    .spuId(skuInfo.getSpuId())
                    .spuName(skuInfo.getSkuName())
                    .spuPic(null) // 订单不需要商品的spu介绍大图
                    .spuBrandId(skuInfo.getBrandId())
                    .categoryId(skuInfo.getCatalogId())
                    .skuId(cartItem.getSkuId())
                    .skuName(cartItem.getTitle())
                    .skuPic(cartItem.getDefaultImage())
                    .skuPrice(cartItem.getPrice())
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

        // 7. 清除确认页缓存
        RedisUtils.deleteObject(CONFIRM_KEY_PREFIX + memberId);

        return orderSn;
    }

    /**
     * 获取购物车项并设置库存状态（不剔除缺货商品，由调用方决定是否过滤）
     */
    private CompletableFuture<List<RmeCartItemVo>> fetchCartItemsWithStock(List<Long> skuIds) {
        return CompletableFuture.supplyAsync(() -> {
            List<RmeCartItemVo> cartItemList = remoteShopCartService.getCartItemList();
            Map<Long, Long> skuAvailableStock = remoteWareService.getSkuAvailableStock(skuIds);
            Set<Long> skuIdSet = Set.copyOf(skuIds);
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
