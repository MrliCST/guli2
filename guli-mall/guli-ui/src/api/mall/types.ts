// ===================== ES 检索参数 =====================

/** 价格区间 */
export interface SkuPriceRange {
  min: string
  max: string
}

/** 筛选条件 */
export interface SearchFilter {
  /** 分类 */
  category?: string
  /** 是否只查询有货商品 */
  hasStock?: boolean
  /** 价格区间 */
  skuPrice?: SkuPriceRange
  /** 品牌id集合 */
  brandIds?: number[]
  /** 属性筛选 key: 属性标识 value: 属性值列表 */
  attributes?: Record<string, string[]>
}

/** 排序单元 */
export interface SortInfo {
  /** 排序标签: hot / sales / price */
  sortTag: string
  /** 排序附属参数: price价格排序存放区间信息 100_noLimit; 热度、销量传空 */
  sortParam?: string
  /** 排序方向: asc / desc */
  sortOrder?: string
}

/** ES 商品检索参数 */
export interface ESearchParam {
  /** 全文检索关键词 */
  keyword?: string
  /** 筛选条件 */
  filter?: SearchFilter
  /** 多条件排序集合 */
  sortList?: SortInfo[]
}

// ===================== ES 检索结果 =====================

/** SKU ES 文档嵌套属性 */
export interface SkuEsModelAttr {
  attrId: number
  attrName: string
  attrValue: string
}

/** SKU 检索文档模型 */
export interface SkuEsModel {
  skuId: number
  spuId: number
  skuPrice: number
  skuImg: string
  saleCount: number
  hasStock: boolean
  hotScore: number
  brandId: number
  catalogId: number
  brandImg: string
  skuTitle: string
  brandName: string
  catalogName: string
  attrs: SkuEsModelAttr[]
}

/** 品牌信息 */
export interface BrandInfo {
  brandId: number
  brandName: string
  brandImg: string
}

/** 属性信息 */
export interface AttrInfo {
  attrId: number
  attrName: string
  attrValue: string[]
}

/** 分类信息 */
export interface CategoryInfo {
  categoryId: number
  categoryName: string
}

/** ES 检索结果 VO */
export interface ESearchListVo {
  total: number
  pageNum: number
  pageSize: number
  products: SkuEsModel[]
  brands: BrandInfo[]
  attrs: AttrInfo[]
  categories: CategoryInfo[]
}

// ===================== SKU 详情 =====================

/** SKU 信息 */
export interface PmsSkuInfo {
  skuId: number
  spuId: number
  skuName: string
  skuDesc?: string
  catalogId: number
  brandId: number
  skuDefaultImg?: string
  skuTitle?: string
  skuSubtitle?: string
  price: number
  saleCount: number
}

/** SKU 图片 */
export interface PmsSkuImages {
  id: number
  skuId: number
  imgUrl: string
  imgSort: number
  defaultImg: number
}

/** SPU 介绍 */
export interface PmsSpuInfoDesc {
  spuId: number
  decript: string
}

/** SPU 销售属性 */
export interface SkuItemSaleAttr {
  attrId: number
  attrName: string
  attrValues: string[]
}

/** SPU 基本属性 */
export interface SpuBaseAttr {
  attrName: string
  attrValue: string
}

/** SPU 基本属性分组 */
export interface SpuBaseAttrGroup {
  groupName: string
  attrs: SpuBaseAttr[]
}

/** SKU 详情 VO */
export interface PmsSkuItemVo {
  skuInfo: PmsSkuInfo
  skuImages: PmsSkuImages[]
  skuItemSaleAttr: SkuItemSaleAttr[]
  spuInfoDesc: PmsSpuInfoDesc
  spuBaseAttrGroup: SpuBaseAttrGroup[]
}
