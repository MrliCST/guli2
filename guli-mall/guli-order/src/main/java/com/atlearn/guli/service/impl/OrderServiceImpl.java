package com.atlearn.guli.service.impl;

import com.atlearn.guli.RemoteMemberService;
import com.atlearn.guli.RemoteShopCartService;
import com.atlearn.guli.RemoteWareService;
import com.atlearn.guli.core.UserInfoContext;
import com.atlearn.guli.domain.vo.OrderConfirmVo;
import com.atlearn.guli.domain.vo.RmeCartItemVo;
import com.atlearn.guli.domain.vo.RmeMemberReceiveAddressVO;
import com.atlearn.guli.exception.BusinessException;
import com.atlearn.guli.exception.ErrorCodeEnum;
import com.atlearn.guli.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
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

    private final Executor executor;

    /**
     * 展示订单，以供用户确认
     */
    @Override
    public OrderConfirmVo confirmOrder(List<Long> skuIds) {
        Long memberId = UserInfoContext.getUserId();

        // 并行获取收货地址和购物车列表
        CompletableFuture<List<RmeMemberReceiveAddressVO>> addressFuture = CompletableFuture.supplyAsync(
            () -> remoteMemberService.getReceiveAddressList(memberId), executor
        ).exceptionally(throwable -> {
            log.error("获取收货地址失败", throwable);
            throw new BusinessException(ErrorCodeEnum.ORDER_CONFIRM_FAILED);
        });

        CompletableFuture<List<RmeCartItemVo>> cartFuture = CompletableFuture.supplyAsync(() -> {
            List<RmeCartItemVo> cartItemList = remoteShopCartService.getCartItemList();
            // 查询有货的 sku | map[skuid] => stockNum
            Map<Long, Long> skuAvailableStock = remoteWareService.getSkuAvailableStock(skuIds);
            // 用户选中的 sku
            Set<Long> skuIdSet = Set.copyOf(skuIds);
            return cartItemList.stream()
                .filter(item -> skuIdSet.contains(item.getSkuId()))
                .map(item -> {
                    item.setHasStock(skuAvailableStock.getOrDefault(item.getSkuId(), 0L) > 0);
                    return item;
                })
                .collect(Collectors.toList());
        }, executor).exceptionally(throwable -> {
            log.error("获取购物车或库存信息失败", throwable);
            throw new BusinessException(ErrorCodeEnum.ORDER_CONFIRM_FAILED);
        });

        CompletableFuture.allOf(addressFuture, cartFuture).join();

        List<RmeMemberReceiveAddressVO> addressList = addressFuture.join();
        List<RmeCartItemVo> selectedItems = cartFuture.join();

        // 计算金额
        BigDecimal productAmount = selectedItems.stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getCount())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal shippingFee = remoteWareService.getShippingFee("河北地质大学");
        BigDecimal totalAmount = productAmount.add(shippingFee);

        return OrderConfirmVo.builder()
            .memberReceiveAddressList(addressList)
            .cartItemList(selectedItems)
            .totalAmount(totalAmount)
            .payAmount(totalAmount)
            .build();
    }
}
