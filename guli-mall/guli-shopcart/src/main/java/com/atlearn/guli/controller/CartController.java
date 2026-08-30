package com.atlearn.guli.controller;

import com.atlearn.guli.domain.ShopCart;
import com.atlearn.guli.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * @param skuId skuId
     * @return 操作结果
     */
    @PostMapping("/addCart/{skuId}")
    public R<Void> addToCart(@PathVariable Long skuId) {
        cartService.addToCart(skuId);
        return R.ok();
    }

    /**
     * 更新购物车商品数量
     *
     * @param skuId skuId
     * @param type  操作类型：incr 加一 / decr 减一
     * @return 操作结果
     */
    @PostMapping("/updateForCart")
    public R<Void> updateForCart(@RequestParam Long skuId,
                                 @RequestParam String type) {
        cartService.updateForCart(skuId, type);
        return R.ok();
    }

    /**
     * 从购物车删除商品
     *
     * @param skuId skuId
     * @return 操作结果
     */
    @PostMapping("/deleteCart/{skuId}")
    public R<Void> deleteFromCart(@PathVariable Long skuId) {
        cartService.deleteFromCart(skuId);
        return R.ok();
    }

    /**
     * 查看购物车
     *
     * @return 购物车对象
     */
    @GetMapping("/cart")
    public R<ShopCart> getCart() {
        return R.ok(cartService.getCart());
    }

}
