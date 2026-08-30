package com.atlearn.guli.service.impl;

import cn.hutool.json.JSONUtil;
import com.atlearn.guli.RemoteMemberService;
import com.atlearn.guli.RemoteShopCartService;
import com.atlearn.guli.core.UserInfoContext;
import com.atlearn.guli.domain.OmsOrder;
import com.atlearn.guli.domain.OmsOrderItem;
import com.atlearn.guli.domain.vo.RmeCartItemVo;
import com.atlearn.guli.domain.vo.RmeMemberReceiveAddressVO;
import com.atlearn.guli.mapper.OmsOrderItemMapper;
import com.atlearn.guli.mapper.OmsOrderMapper;
import com.atlearn.guli.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 订单服务实现
 *
 * @author guli
 */
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements IOrderService {

    @DubboReference
    private RemoteMemberService remoteMemberService;

    @DubboReference
    private RemoteShopCartService remoteShopCartService;

    private final OmsOrderMapper omsOrderMapper;
    private final OmsOrderItemMapper omsOrderItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OmsOrder createOrder() {
        Long memberId = UserInfoContext.getUserId();

        // 并行获取收货地址和购物车列表
        CompletableFuture<List<RmeMemberReceiveAddressVO>> addressFuture = CompletableFuture.supplyAsync(
            () -> remoteMemberService.getReceiveAddressList(memberId));
        CompletableFuture<List<RmeCartItemVo>> cartFuture = CompletableFuture.supplyAsync(
            () -> remoteShopCartService.getCartItemList());

        CompletableFuture.allOf(addressFuture, cartFuture).join();

        List<RmeMemberReceiveAddressVO> addressList = addressFuture.join();
        List<RmeCartItemVo> cartItemList = cartFuture.join();

        // 计算订单金额
        BigDecimal totalAmount = cartItemList.stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getCount())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 选取默认收货地址
        RmeMemberReceiveAddressVO address = addressList.stream()
            .filter(a -> a.getDefaultStatus() != null && a.getDefaultStatus() == 1)
            .findFirst()
            .orElse(addressList.isEmpty() ? null : addressList.get(0));

        // 生成订单号
        String orderSn = UUID.randomUUID().toString().replace("-", "");

        // 构建订单
        OmsOrder order = OmsOrder.builder()
            .memberId(memberId)
            .orderSn(orderSn)
            .totalAmount(totalAmount)
            .payAmount(totalAmount)
            .freightAmount(BigDecimal.ZERO)
            .promotionAmount(BigDecimal.ZERO)
            .integrationAmount(BigDecimal.ZERO)
            .couponAmount(BigDecimal.ZERO)
            .discountAmount(BigDecimal.ZERO)
            .sourceType(0)
            .status(0)
            .confirmStatus(0)
            .deleteStatus(0)
            .integration(0)
            .growth(0)
            .receiverName(address != null ? address.getName() : null)
            .receiverPhone(address != null ? address.getPhone() : null)
            .receiverPostCode(address != null ? address.getPostCode() : null)
            .receiverProvince(address != null ? address.getProvince() : null)
            .receiverCity(address != null ? address.getCity() : null)
            .receiverRegion(address != null ? address.getRegion() : null)
            .receiverDetailAddress(address != null ? address.getDetailAddress() : null)
            .build();

        omsOrderMapper.insert(order);

        // 构建订单项
        List<OmsOrderItem> orderItems = cartItemList.stream().map(item -> OmsOrderItem.builder()
            .orderId(order.getId())
            .orderSn(orderSn)
            .skuId(item.getSkuId())
            .skuName(item.getTitle())
            .skuPic(item.getDefaultImage())
            .skuPrice(item.getPrice())
            .skuQuantity(item.getCount())
            .realAmount(item.getPrice().multiply(BigDecimal.valueOf(item.getCount())))
            .promotionAmount(BigDecimal.ZERO)
            .couponAmount(BigDecimal.ZERO)
            .integrationAmount(BigDecimal.ZERO)
            .giftIntegration(0)
            .giftGrowth(0)
            .skuAttrsVals(JSONUtil.toJsonStr(item.getSaleAttr()))
            .build()
        ).collect(Collectors.toList());

        orderItems.forEach(omsOrderItemMapper::insert);

        return order;
    }

}
