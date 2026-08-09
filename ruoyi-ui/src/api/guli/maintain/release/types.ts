// ======================== SPU 大对象 ========================

import type { OssVO } from '@/api/system/oss/types'

export interface ReleaseForm extends BaseEntity {
  // 第一步: 基本信息
  basicInfo?: SpuInfo

  // 第二步: 基本属性
  baseAttrs?: BaseAttr[]

  // 第三步: 销售属性
  saleAttrs?: SaleAttr[]

  // 第四步: SKU信息
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
}

export interface BaseAttr {
  attrId?: string | number
  attrValue?: string
}

export interface SaleAttr {
  attrId?: string | number
  attrValue?: string
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
