package com.atlearn.guli.controller;

import com.atlearn.guli.domain.CartItem;
import com.atlearn.guli.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 购物车控制器
 *
 * @author guli
 */
@RequiredArgsConstructor
@RestController
public class CartController {

    private final ICartService cartService;

    /**
     * 添加商品到购物车
     *
     * @param cartItem 购物车项
     * @return 操作结果
     */
    @PostMapping("/addCart")
    public R<Void> addCart(@RequestBody CartItem cartItem) {
        cartService.addCart(cartItem);
        return R.ok();
    }

}
