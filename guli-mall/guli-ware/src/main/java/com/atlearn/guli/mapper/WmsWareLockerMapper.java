package com.atlearn.guli.mapper;

import com.atlearn.guli.domain.dto.LockItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 库存锁定 Mapper（JdbcTemplate 实现）
 * 批量 CAS 锁定/解锁库存，返回每条 UPDATE 的影响行数
 *
 * @author guli
 */
@Repository
@RequiredArgsConstructor
public class WmsWareLockerMapper {

    private final JdbcTemplate jdbcTemplate;

    private static final String LOCK_SQL =
        "UPDATE wms_ware_sku SET stock_locked = stock_locked + ? "
        + "WHERE ware_id = ? AND sku_id = ? AND (stock - stock_locked) >= ?";

    private static final String UNLOCK_SQL =
        "UPDATE wms_ware_sku SET stock_locked = stock_locked - ? "
        + "WHERE ware_id = ? AND sku_id = ? AND stock_locked >= ?";

    /**
     * 批量 CAS 锁定库存
     *
     * @param items 锁定明细列表
     * @return 每条 UPDATE 的影响行数，0 表示库存不足或记录不存在
     */
    public int[] batchLockStock(List<LockItemDto> items) {
        List<Object[]> batchArgs = toBatchArgs(items);
        return jdbcTemplate.batchUpdate(LOCK_SQL, batchArgs);
    }

    /**
     * 批量 CAS 解锁库存
     *
     * @param items 解锁明细列表
     * @return 每条 UPDATE 的影响行数，0 表示解锁失败
     */
    public int[] batchUnlockStock(List<LockItemDto> items) {
        List<Object[]> batchArgs = toBatchArgs(items);
        return jdbcTemplate.batchUpdate(UNLOCK_SQL, batchArgs);
    }

    /**
     * DTO 转 SQL 参数数组
     */
    private List<Object[]> toBatchArgs(List<LockItemDto> items) {
        return items.stream()
            .map(item -> new Object[]{
                item.getLockNum(),
                item.getWareId(),
                item.getSkuId(),
                item.getLockNum()
            })
            .collect(Collectors.toList());
    }
}
