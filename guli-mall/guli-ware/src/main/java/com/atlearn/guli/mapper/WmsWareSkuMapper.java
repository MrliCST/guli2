package com.atlearn.guli.mapper;

import java.util.List;

import com.atlearn.guli.domain.WmsWareSku;
import com.atlearn.guli.domain.bo.RmeLockWareBo;
import com.atlearn.guli.domain.dto.SkuIdToStockDTO;
import com.atlearn.guli.domain.vo.RmeWareSkuVo;
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
     * 根据 skuId 列表批量查询各仓库库存信息（含可用库存），按可用库存降序排列
     *
     * @param skuIds skuId 列表
     * @return 仓库库存列表
     */
    List<RmeWareSkuVo> getWareSkuListBySkuIds(@Param("skuIds") List<Long> skuIds);

    /**
     * 批量锁定仓库库存（增加 stock_locked）
     *
     * @param list 锁定信息
     * @return 受影响总行数
     */
    int lockWareSkuBatch(@Param("list") List<RmeLockWareBo> list);

}


