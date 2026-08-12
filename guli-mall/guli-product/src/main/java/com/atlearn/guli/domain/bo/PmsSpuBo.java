package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.PmsSpuInfo;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "spu信息不能为null")
    private SpuInfo spu;

    @NotEmpty(message = "sku列表信息不能为空或者null")
    private List<Sku> skus;

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
        private List<BaseAttr> baseAttrs;
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
        private List<String> skuImages;
        private List<SaleAttr> skuAttrs;
    }

    @Data
    public static class BaseAttr {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }

    @Data
    public static class SaleAttr {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
