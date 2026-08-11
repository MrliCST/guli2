// ======================== SPU 大对象 ========================

import type { OssVO } from '@/api/system/oss/types'

export interface ReleaseForm extends BaseEntity {
  // 第一步: 基本信息（含基本属性）
  spu?: SpuInfo

  // 第四步: SKU信息（销售属性已嵌入每个SKU中）
  skus?: Sku[]
}

// ======================== 子对象 ========================

export interface SpuInfo {
  spuName?: string
  spuDescription?: string
  catalogId?: string | number
  brandId?: string | number
  weight?: number
  publishStatus?: number
  MainImgDesc?: string
  ImgAlbum?: OssVO[]
  /** 选中的基本属性 */
  baseAttrs?: BaseAttr[]
}

export interface Sku {
  skuName?: string
  skuDesc?: string
  skuDefaultImg?: string
  skuTitle?: string
  skuSubtitle?: string
  price?: string
  stock?: number
  skuImages?: string[]
  /** 该SKU的销售属性组合 */
  skuAttrs?: SaleAttr[]
}

export interface BaseAttr {
  attrId?: string | number
  attrName?: string
  attrValue?: string
}

export interface SaleAttr {
  attrId?: string | number
  attrName?: string
  attrValue?: string
}

// ======================== 分组带属性查询结果 VO ========================

export interface AttrGroupWithAttrsVO {
  attrGroupId: string | number
  attrGroupName: string
  sort: number
  descript: string
  icon: string
  catelogId: string | number
  /** 该分组下的属性列表 */
  attrs: AttrVO[]
}

export interface AttrVO {
  attrId: string | number
  attrName: string
  searchType: number
  valueType: number
  icon: string
  valueSelect: string
  attrType: number
  enable: number
  catelogId: string | number
  showDesc: number
}
