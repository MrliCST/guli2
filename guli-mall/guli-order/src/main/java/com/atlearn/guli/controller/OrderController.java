package com.atlearn.guli.controller;

import com.atlearn.guli.domain.OmsOrder;
import com.atlearn.guli.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 创建订单
     *
     * @return 订单
     */
    @PostMapping("/order/create")
    public R<OmsOrder> createOrder() {
        return R.ok(orderService.createOrder());
    }

}
