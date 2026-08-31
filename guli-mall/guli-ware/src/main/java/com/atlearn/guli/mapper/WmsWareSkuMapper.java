package com.atlearn.guli.mapper;

import java.util.List;

import com.atlearn.guli.domain.WmsWareSku;
import com.atlearn.guli.domain.dto.SkuIdToStockDTO;
import com.atlearn.guli.domain.vo.WmsWareSkuVo;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 商品库存Mapper接口
 *
 * @author mayao
 * @date 2026-08-12
 */
public interface WmsWareSkuMapper extends BaseMapperPlus<WmsWareSku, WmsWareSkuVo> {

    /**
     * 按 sku 分组查询可用库存（库存数 - 锁定库存）
     *
     * @param skuIds sku id 集合
     * @return sku 可用库存列表
     */
    List<SkuIdToStockDTO> getSkuIdToStockMap(@Param("skuIds") List<Long> skuIds);

    /**
     * 查询某 sku 所在的所有仓库（按 id 排序）
     *
     * @param skuId skuId
     * @return 仓库库存列表
     */
    List<WmsWareSku> listWareBySkuId(@Param("skuId") Long skuId);

    /**
     * 乐观锁锁定库存
     *
     * @param id  库存记录id
     * @param num 锁定数量
     * @return 受影响行数
     */
    int lockStock(@Param("id") Long id, @Param("num") Long num);

    /**
     * 解锁库存
     *
     * @param id  库存记录id
     * @param num 解锁数量
     * @return 受影响行数
     */
    int unlockStock(@Param("id") Long id, @Param("num") Long num);

}
