package com.atlearn.guli.dto;

import java.util.List;
import lombok.Data;

@Data
public class ESearchListVo {
    // 分页参数
    private Long total;
    private Integer pageNum;
    private Integer pageSize;

    // 检索的商品
    private List<SkuEsModel> products;

    // 可选择的品牌信息（例如: 小米，华为，oppo等）
    private List<BrandInfo> brands;

    // 可选择的属性信息（基本属性，销售属性等）
    private List<AttrInfo> attrs;

    // 可选择的分类信息（例如：手机，电脑等）
    private List<CategoryInfo> categories;

    @Data
    public static class BrandInfo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    public static class AttrInfo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    @Data
    public static class CategoryInfo {
        private Long categoryId;
        private String categoryName;
    }
}
