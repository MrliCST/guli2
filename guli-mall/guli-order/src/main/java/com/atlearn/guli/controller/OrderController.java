package com.atlearn.guli.controller;

import com.atlearn.guli.domain.vo.OrderConfirmVo;
import com.atlearn.guli.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
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

}
