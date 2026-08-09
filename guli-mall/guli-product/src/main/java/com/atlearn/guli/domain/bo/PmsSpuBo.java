package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.PmsSpuInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.dromara.resource.api.domain.RemoteFile;
import java.util.List;

/**
 * SPU信息业务对象 pms_spu_info
 *
 * @author mayao
 * @date 2026-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PmsSpuInfo.class, reverseConvertGenerate = false)
public class PmsSpuBo extends BaseEntity {

    // ======================== 第一步: 基本信息 ========================

    private SpuInfo spuInfo;

    // ======================== 第二步: 基本属性 ========================

    private List<BaseAttr> baseAttrs;

    // ======================== 第三步: 销售属性 ========================

    private List<SaleAttr> saleAttrs;

    // ======================== 第四步: SKU信息 ========================

    private List<Sku> skus;

    // ======================== 内部类 ========================

    @Data
    public static class SpuInfo {
        private String spuName;
        private String spuDescription;
        private Long catalogId;
        private Long brandId;
        private Long weight;
        private Long publishStatus;
        private String MainImgDesc;
        private List<RemoteFile> ImgAlbum;
    }

    @Data
    public static class BaseAttr {
        private Long attrId;
        private String attrValue;
    }

    @Data
    public static class SaleAttr {
        private Long attrId;
        private String attrValue;
    }

    @Data
    public static class Sku {
        private String skuName;
        private String skuDesc;
        private String skuDefaultImg;
        private String skuTitle;
        private String skuSubtitle;
        private String price;
        private Integer stock;
        private String[] skuImages;
    }
}
