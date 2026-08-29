package com.atlearn.guli.constant;

/**
 * ES 检索相关常量
 *
 * @author guli
 */
public class GuliEsConstant {

    private GuliEsConstant() {
    }

    // ==================== 分页 ====================
    public static final int PAGE_NUM = 5;

    // ==================== 索引与字段 ====================
    public static final String INDEX_NAME = "sku_index";

    public static final String FIELD_SKU_TITLE = "skuTitle";
    public static final String FIELD_CATALOG_ID = "catalogId";
    public static final String FIELD_CATALOG_NAME = "catalogName";
    public static final String FIELD_HAS_STOCK = "hasStock";
    public static final String FIELD_SKU_PRICE = "skuPrice";
    public static final String FIELD_BRAND_ID = "brandId";
    public static final String FIELD_BRAND_NAME = "brandName";
    public static final String FIELD_BRAND_IMG = "brandImg";
    public static final String FIELD_HOT_SCORE = "hotScore";
    public static final String FIELD_SALE_COUNT = "saleCount";

    // ==================== nested 路径与子字段 ====================
    public static final String NESTED_PATH_ATTRS = "attrs";
    public static final String FIELD_ATTR_ID = "attrs.attrId";
    public static final String FIELD_ATTR_NAME = "attrs.attrName";
    public static final String FIELD_ATTR_VALUE = "attrs.attrValue";

    // ==================== 聚合名称 ====================
    public static final String AGG_BRAND = "brand_agg";
    public static final String AGG_BRAND_NAME = "brand_name_agg";
    public static final String AGG_BRAND_IMG = "brand_img_agg";
    public static final String AGG_CATEGORY = "category_agg";
    public static final String AGG_CATEGORY_NAME = "category_name_agg";
    public static final String AGG_ATTR = "attr_agg";
    public static final String AGG_ATTR_ID = "attr_id_agg";
    public static final String AGG_ATTR_NAME = "attr_name_agg";
    public static final String AGG_ATTR_VALUE = "attr_value_agg";

    // ==================== 聚合分桶大小 ====================
    public static final int AGG_BUCKET_SIZE = 10;

    // ==================== 排序与高亮 ====================
    public static final String SORT_TAG_HOT = "hot";
    public static final String SORT_TAG_SALES = "sales";
    public static final String SORT_TAG_PRICE = "price";
    public static final String SORT_ORDER_ASC = "asc";

    public static final String HL_PRE_TAG = "<b style='color:red'>";
    public static final String HL_POST_TAG = "</b>";

}
