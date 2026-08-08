package com.atlearn.guli.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * spu信息对象 pms_spu_info
 *
 * @author mayao
 * @date 2026-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pms_spu_info")
public class PmsSpuInfo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 商品名称
     */
    private String spuName;

    /**
     * 商品描述
     */
    private String spuDescription;

    /**
     * 所属分类id
     */
    private Long catalogId;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 重量
     */
    private Long weight;

    /**
     * 上架状态[0-下架，1-上架]
     */
    private Long publishStatus;

}
