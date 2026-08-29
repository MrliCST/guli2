package com.atlearn.guli.service.impl;

import com.atlearn.guli.core.UserInfoContext;
import com.atlearn.guli.domain.CartItem;
import com.atlearn.guli.exception.BusinessException;
import com.atlearn.guli.exception.ErrorCodeEnum;
import com.atlearn.guli.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.redis.utils.RedisUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 购物车服务实现
 * <p>
 * Redis 结构：key = gulimall:cart:{userId}，value = Map<Long, CartItem>
 * 其中 Map 的 key 为 skuId，value 为 CartItem
 *
 * @author guli
 */
@RequiredArgsConstructor
@Service
public class CartServiceImpl implements ICartService {

    /** Redis 购物车 key 前缀 */
    private static final String CART_KEY_PREFIX = "gulimall:cart:";

    @Override
    public void addCart(CartItem cartItem) {
        Long userId = UserInfoContext.getUserId();
        if (userId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        String cartKey = CART_KEY_PREFIX + userId;

        // 从redis取出整个购物车map
        Map<String, CartItem> cartMap = RedisUtils.getCacheMap(cartKey);
        if (cartMap == null) {
            cartMap = new HashMap<>();
        }

        // 累加数量
        String skuKey = String.valueOf(cartItem.getSkuId());
        CartItem existing = cartMap.get(skuKey);
        if (existing != null) {
            cartItem.setCount(existing.getCount() + cartItem.getCount());
        }
        cartMap.put(skuKey, cartItem);

        // 序列化存回redis
        RedisUtils.setCacheMap(cartKey, cartMap);
    }

}
