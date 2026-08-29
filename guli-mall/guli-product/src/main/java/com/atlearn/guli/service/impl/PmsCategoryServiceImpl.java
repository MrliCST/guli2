package com.atlearn.guli.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.redis.utils.RedisUtils;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.atlearn.guli.domain.bo.PmsCategoryBo;
import com.atlearn.guli.domain.vo.PmsCategoryVo;
import com.atlearn.guli.domain.PmsCategory;
import com.atlearn.guli.domain.PmsCategoryBrandRelation;
import com.atlearn.guli.mapper.PmsCategoryMapper;
import com.atlearn.guli.mapper.PmsCategoryBrandRelationMapper;
import com.atlearn.guli.service.IPmsCategoryService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商品三级分类Service业务层处理
 *
 * @author mayao
 * @date 2026-07-25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PmsCategoryServiceImpl implements IPmsCategoryService {

    private final PmsCategoryMapper baseMapper;
    private final PmsCategoryBrandRelationMapper cbrMapper;
    private final RedissonClient redissonClient;

    /**
     * 查询商品三级分类
     *
     * @param catId 主键
     * @return 商品三级分类
     */
    @Override
    public PmsCategoryVo queryById(Long catId){
        return baseMapper.selectVoById(catId);
    }

    /**
     * 查询符合条件的商品三级分类列表
     * @param bo 查询条件
     * @return 三级分类列表
     */
    @Override
    @Cacheable(value = "category:tree", key = "#root.method.name")
    public List<PmsCategoryVo> queryList(PmsCategoryBo bo) {
        LambdaQueryWrapper<PmsCategory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询符合条件的商品三级分类树列表
     *
     * @param bo 查询条件
     * @return 商品三级分类树列表
     */
    @SuppressWarnings("null") // 抑制null警告的注解
    @Cacheable(cacheNames = {"category:tree"}, key = "#root.method.name", sync = true)
    public List<PmsCategoryVo> queryTreeList(PmsCategoryBo bo){
        LambdaQueryWrapper<PmsCategory> lqw = buildQueryWrapper(bo);
        List<PmsCategoryVo> list = baseMapper.selectVoList(lqw);  // 获取P数组

        // return R -> roof+
        // roof -> P      (if P.parentId == 0)
        // P -> field P+  (if Pleft.cid == Pright.parentId)

        // 按 parentCid相同为一组 进行分组，用于构建树形
        // 父节点cid -> 子节点列表
        Map<Long, List<PmsCategoryVo>> parentChildMap = list.stream()
                .collect(Collectors.groupingBy(x -> x.getParentCid()));
        Comparator<PmsCategoryVo> sortComparator = Comparator.comparing(
            PmsCategoryVo::getSort, Comparator.nullsLast(Long::compareTo));

        parentChildMap.forEach((parentId, children) ->
                children.sort(sortComparator));

        // 为每一个父节点填充已排序的子节点, 获取顶级父节点
        List<PmsCategoryVo> tree = list.stream()
            .map(vo -> {
                vo.setChildren(parentChildMap.getOrDefault(vo.getCatId(), List.of()));
                return vo;
            })
            .filter(vo -> vo.getParentCid() == 0L)
            .sorted(sortComparator)
            .collect(Collectors.toList());
        
        return tree;
    }

    @SuppressWarnings("null")  // PmsCategory::getCatId方法由@Data生成，不可能为空，取消警告
    private LambdaQueryWrapper<PmsCategory> buildQueryWrapper(PmsCategoryBo bo) {
        LambdaQueryWrapper<PmsCategory> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getCatId() != null, PmsCategory::getCatId, bo.getCatId());  // where catId = ?
        lqw.like(StringUtils.isNotBlank(bo.getName()), PmsCategory::getName, bo.getName());  // where name like ?
        lqw.orderByAsc(PmsCategory::getCatId);  // order by catId asc
        return lqw;
    }

    /**
     * 新增商品三级分类
     *
     * @param bo 商品三级分类
     * @return 是否新增成功
     */
    @Override
    @Caching(evict = {
        @CacheEvict(cacheNames = {"category:tree"}, key="queryList"),
        @CacheEvict(cacheNames = {"category:tree"}, key="queryTreeList")
    })
    public Boolean insertByBo(PmsCategoryBo bo) {
        PmsCategory add = MapstructUtils.convert(bo, PmsCategory.class);
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    /**
     * 修改商品三级分类
     *
     * @param bo 商品三级分类
     * @return 是否修改成功
     */
    @Override
    @Transactional
    @SuppressWarnings("null")
    @CacheEvict(cacheNames = {"category:tree"}, allEntries = true)
    public Boolean updateByBo(PmsCategoryBo bo) {
        PmsCategory update = MapstructUtils.convert(bo, PmsCategory.class);
        validEntityBeforeSave(update);
        boolean flag = baseMapper.updateById(update) > 0;
        if (!flag || bo.getName() == null) {
            return flag;
        }
        // 级联更新中间表的冗余分类名
        cbrMapper.update(null,
            Wrappers.lambdaUpdate(PmsCategoryBrandRelation.class)
                .set(PmsCategoryBrandRelation::getCatelogName, bo.getName())
                .eq(PmsCategoryBrandRelation::getCatelogId, bo.getCatId()));
        return true;
    }

    /**
     * 批量修改商品三级分类 (sort批量更新)
     *
     * @param boList 商品三级分类集合
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(cacheNames = {"category:tree"}, allEntries = true)
    public Boolean updateBatchByBo(List<PmsCategoryBo> boList) {
        List<PmsCategory> updateList = boList.stream()
            .map(bo -> MapstructUtils.convert(bo, PmsCategory.class))
            .toList();
        updateList.forEach(this::validEntityBeforeSave);
        return baseMapper.updateBatchById(updateList);
    }

    /**
     * 校验并批量删除商品三级分类信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @CacheEvict(cacheNames = {"category:tree"}, allEntries = true)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PmsCategory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 不采纳的，练习原生redis的分布锁实现
     * 作者: 马瑶
     */
    @Deprecated
    private List<PmsCategoryVo> oldQueryTreeList(PmsCategoryBo bo) {
        String cacheKey = "category:tree";
        String lockKey  = "category:tree:lock";
        
        // 1. 查缓存，命中直接返回
        List<PmsCategoryVo> tree = RedisUtils.getCacheObject("category:tree");
        if (CollUtil.isNotEmpty(tree)) {
            return tree;
        }

        /*  
            1. redis穿透: 用户访问了 mysql 不存在的数据，该数据不能记录到缓存中，导致缓存穿透
            + 解决方案：缓存空对象

            2. redis雪崩: 缓存中大量数据同时过期，导致大量请求直接访问 mysql，导致 mysql 崩溃
            + 解决方案：设置过期时间随机值，避免大量数据同时过期

            3. redis击穿: 缓存中某个热点数据过期，导致大量请求直接访问 mysql，导致 mysql 崩溃
            + 解决方案：设置锁，只让第一个请求去查询 mysql 并缓存，其他等待缓存中数据
        */

        // 2. 抢锁，抢不到则自旋
        String uuid = UUID.randomUUID().toString();
        while (!RedisUtils.setObjectIfAbsent(lockKey, uuid, Duration.ofSeconds(5))) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("抢锁被中断", e);
            }
        }

        try {
            // 3. 抢到锁后 double-check（可能是前一个线程刚写入）
            tree = RedisUtils.getCacheObject(cacheKey);
            if (CollUtil.isNotEmpty(tree)) {
                return tree;
            }
            // 4. 临界区：查库 + 写缓存
            // tree = queryTreeData(bo);
            int randomSeconds = RandomUtil.randomInt(40, 70);
            RedisUtils.setCacheObject(cacheKey, tree ,Duration.ofSeconds(randomSeconds));
            return tree;
        } finally {
            // 5. 无论如何都要释放锁，防止查库异常时锁泄漏
            String Lua = """
                if redis.call("get", KEYS[1]) == ARGV[1]
                then return redis.call("del", KEYS[1])
                else return 0
                end
            """;
            RedisUtils.getClient().getScript().eval(
                RScript.Mode.READ_WRITE, 
                Lua, 
                RScript.ReturnType.INTEGER,
                Collections.singletonList(lockKey),  // KEYS列表
                uuid   // arg...
            );
        }
    }

    /**
     * 再次不采纳的，练习redisson的分布锁使用
     * 作者: 马瑶
     */
    @Deprecated
    private List<PmsCategoryVo> oldAgainQueryTreeList(PmsCategoryBo bo) {
        RMapCache<String, List<PmsCategoryVo>> cache = redissonClient.getMapCache("category");
        String cacheKey = "treeData";

        // 1. 查缓存，命中直接返回
        List<PmsCategoryVo> tree = cache.get(cacheKey);
        if (CollUtil.isNotEmpty(tree)) {
            return tree;
        }

        // 2. 加锁防击穿
        RLock lock = redissonClient.getLock("category:tree:lock");
        lock.lock();
        try {
            // 3. 二次检查：等锁期间前一个线程可能已经写入了缓存
            tree = cache.get(cacheKey);
            if (CollUtil.isNotEmpty(tree)) {
                return tree;
            }
            // 4. 查库 + 写缓存（随机 TTL 防雪崩）
            // tree = queryTreeData(bo);
            cache.put(cacheKey, tree, RandomUtil.randomInt(40, 70), TimeUnit.SECONDS);
            return tree;
        } finally {
            lock.unlock();
        }
    }
}
