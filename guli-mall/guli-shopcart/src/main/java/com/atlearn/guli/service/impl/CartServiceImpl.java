package com.atlearn.guli.service.impl;

import com.atlearn.guli.RemoteProductService;
import com.atlearn.guli.core.UserInfoContext;
import com.atlearn.guli.domain.CartItem;
import com.atlearn.guli.domain.ShopCart;
import com.atlearn.guli.domain.vo.RmeSkuInfoVo;
import com.atlearn.guli.domain.vo.RmeSkuSaleAttrValueVo;
import com.atlearn.guli.exception.BusinessException;
import com.atlearn.guli.exception.ErrorCodeEnum;
import com.atlearn.guli.service.ICartService;
import lombok.RequiredArgsConstructor;

import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.common.redis.utils.RedisUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 购物车服务实现
 * 
 * Redis 结构：key = gulimall:cart:{userId}，Hash field = skuId，value = CartItem
 *
 * @author guli
 */
@RequiredArgsConstructor
@Service
public class CartServiceImpl implements ICartService {

    @DubboReference
    private RemoteProductService remoteProductService;

    private final RedissonClient redissonClient;

    /** Redis 购物车 key 前缀 */
    private static final String CART_KEY_PREFIX = "gulimall:cart:";

    /** Redis 购物车锁 key 前缀 */
    private static final String CART_LOCK_PREFIX = "gulimall:cart:lock:";

    /** 购物车数量操作类型：增加 */
    private static final String TYPE_INCR = "incr";

    /** 购物车数量操作类型：减少 */
    private static final String TYPE_DECR = "decr";

    /**
     * 默认用户是在看商品时添加的，只添加一个，后续可以增加
     */
    @Override
    public void addToCart(Long skuId) {
        Long userId = UserInfoContext.getUserId();

        // 并行调用两个远程方法
        CompletableFuture<RmeSkuInfoVo> skuInfoFuture = CompletableFuture.supplyAsync(
            () -> remoteProductService.getSkuInfoBySkuId(skuId));
        CompletableFuture<List<RmeSkuSaleAttrValueVo>> saleAttrFuture = CompletableFuture.supplyAsync(
            () -> remoteProductService.getSkuSaleAttrValueListBySkuId(skuId));

        // 等待两个都完成
        CompletableFuture.allOf(skuInfoFuture, saleAttrFuture).join();

        RmeSkuInfoVo skuInfo = skuInfoFuture.join();
        List<RmeSkuSaleAttrValueVo> saleAttr = saleAttrFuture.join();

        // 构建 CartItem
        String cartKey = CART_KEY_PREFIX + userId;
        String skuKey = String.valueOf(skuId);

        CartItem cartItem = new CartItem();
        cartItem.setSkuId(skuId);
        cartItem.setTitle(skuInfo.getSkuTitle());
        cartItem.setDefaultImage(skuInfo.getSkuDefaultImg());
        cartItem.setPrice(skuInfo.getPrice());
        cartItem.setSaleAttr(saleAttr);
        cartItem.setCount(1);

        // 存入 Redis Hash
        RedisUtils.setCacheMapValue(cartKey, skuKey, cartItem);
    }

    @Override
    public void updateForCart(Long skuId, String type) {
        Long userId = UserInfoContext.getUserId();

        // 分布式锁，防止用户多端同时请求时，导致数据不一致
        // 同 key，串行化更新
        String lockKey = CART_LOCK_PREFIX + userId;
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        try {
            String cartKey = CART_KEY_PREFIX + userId;
            String skuKey = String.valueOf(skuId);

            // 利用 skuId 快速定位 Hash 中的 CartItem
            CartItem existing = RedisUtils.getCacheMapValue(cartKey, skuKey);
            if (existing == null) {
                throw new BusinessException(ErrorCodeEnum.DATA_NOT_FOUND);
            }

            if (!TYPE_INCR.equals(type) && !TYPE_DECR.equals(type)) {
                throw new BusinessException(ErrorCodeEnum.CART_TYPE_INVALID);
            }

            int newCount = TYPE_INCR.equals(type) ? existing.getCount() + 1 : existing.getCount() - 1;
            if (newCount < 1) {
                throw new BusinessException(ErrorCodeEnum.CART_COUNT_MIN);
            }

            existing.setCount(newCount);
            RedisUtils.setCacheMapValue(cartKey, skuKey, existing);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void deleteFromCart(Long skuId) {
        Long userId = UserInfoContext.getUserId();

        String cartKey = CART_KEY_PREFIX + userId;
        String skuKey = String.valueOf(skuId);

        CartItem existing = RedisUtils.getCacheMapValue(cartKey, skuKey);
        if (existing == null) {
            throw new BusinessException(ErrorCodeEnum.DATA_NOT_FOUND);
        }

        RedisUtils.delCacheMapValue(cartKey, skuKey);
    }

    @Override
    @SuppressWarnings("null")
    public ShopCart getCart() {
        Long userId = UserInfoContext.getUserId();
        String cartKey = CART_KEY_PREFIX + userId;

        Map<String, CartItem> cartMap = RedisUtils.getCacheMap(cartKey);
        if (cartMap == null || cartMap.isEmpty()) {
            ShopCart shopCart = new ShopCart();
            shopCart.setItems(Collections.emptyList());
            shopCart.setCountNum(0);
            shopCart.setCountType(0);
            shopCart.setTotalAmount(BigDecimal.ZERO);
            shopCart.setReduce(BigDecimal.ZERO);
            return shopCart;
        }

        List<CartItem> items = new ArrayList<>(cartMap.values());
        int countNum = items.stream()
            .mapToInt(x -> x.getCount())
            .sum();
        BigDecimal totalAmount = items.stream()
            .map(x -> x.getTotalPrice())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        ShopCart shopCart = new ShopCart();
        shopCart.setItems(items);
        shopCart.setCountNum(countNum);
        shopCart.setCountType(items.size());
        shopCart.setTotalAmount(totalAmount);
        shopCart.setReduce(BigDecimal.ZERO);
        return shopCart;
    }

}
