package com.atlearn.guli.dubbo;

import cn.hutool.core.bean.BeanUtil;
import com.atlearn.guli.RemoteShopCartService;
import com.atlearn.guli.core.UserInfoContext;
import com.atlearn.guli.domain.CartItem;
import com.atlearn.guli.domain.vo.RmeCartItemVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.redis.utils.RedisUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 远程购物车服务实现
 *
 * @author guli
 */
@Service
@DubboService
public class RemoteShopCartServiceImpl implements RemoteShopCartService {

    /** Redis 购物车 key 前缀 */
    private static final String CART_KEY_PREFIX = "gulimall:cart:";

    @Override
    public List<RmeCartItemVo> getCartItemList() {
        Long userId = UserInfoContext.getUserId();
        String cartKey = CART_KEY_PREFIX + userId;

        Map<String, CartItem> cartMap = RedisUtils.getCacheMap(cartKey);
        if (cartMap == null || cartMap.isEmpty()) {
            return Collections.emptyList();
        }

        List<CartItem> items = new ArrayList<>(cartMap.values());
        return BeanUtil.copyToList(items, RmeCartItemVo.class);
    }

}
