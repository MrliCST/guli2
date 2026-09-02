package com.atlearn.guli.controller;

import com.atlearn.guli.domain.bo.SubmitOrderBo;
import com.atlearn.guli.domain.vo.OrderConfirmVo;
import com.atlearn.guli.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单控制器
 *
 * @author guli
 */
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final IOrderService orderService;

    /**
     * 订单确认页数据
     *
     * @param skuIds 用户勾选的购物车项 skuId 列表
     * @return 订单确认页数据
     */
    @PostMapping("/order/confirm")
    public R<OrderConfirmVo> confirmOrder(@RequestBody List<Long> skuIds) {
        return R.ok(orderService.confirmOrder(skuIds));
    }

    /**
     * 取消订单
     *
     * @param orderSn 订单号
     * @return 是否取消成功
     */
    @PostMapping("/order/cancel")
    public R<Boolean> cancelOrder(@RequestBody String orderSn) {
        return R.ok(orderService.cancelOrder(orderSn));
    }

    /**
     * 提交订单
     *
     * @param bo 提交订单参数
     * @return 订单号
     */
    @RepeatSubmit()
    @PostMapping("/order/submit")
    public R<String> submitOrder(@RequestBody SubmitOrderBo bo) {
        return R.ok(orderService.submitOrder(bo));
    }

}
