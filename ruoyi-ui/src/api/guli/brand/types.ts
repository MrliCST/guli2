export interface BrandVO {
  /**
   * 品牌id
   */
  brandId: string | number

  /**
   * 品牌名
   */
  name: string

  /**
   * 品牌logo地址
   */
  logo: string

  /**
   * 介绍
   */
  descript: string

  /**
   * 显示状态[0-不显示；1-显示]
   */
  showStatus: number

  /**
   * 检索首字母
   */
  firstLetter: string

  /**
   * 排序
   */
  sort: number
}

export interface BrandForm extends BaseEntity {
  /**
   * 品牌id
   */
  brandId?: string | number

  /**
   * 品牌名
   */
  name?: string

  /**
   * 品牌logo地址
   */
  logo?: string

  /**
   * 介绍
   */
  descript?: string

  /**
   * 显示状态[0-不显示；1-显示]
   */
  showStatus?: number

  /**
   * 检索首字母
   */
  firstLetter?: string

  /**
   * 排序
   */
  sort?: number
}

export interface BrandQuery extends PageQuery {
  /**
   * 品牌名
   */
  name?: string

  /**
   * 日期范围参数
   */
  params?: any
}

export interface CategoryBrandRelationVO {
  /**
   *
   */
  id: string | number;

  /**
   * 品牌id
   */
  brandId: string | number;

  /**
   * 分类id
   */
  catelogId: string | number;

  /**
   *
   */
  brandName: string;

  /**
   *
   */
  catelogName: string;

}

export interface CategoryBrandRelationForm extends BaseEntity {
  /**
   *
   */
  id?: string | number;

  /**
   * 品牌id
   */
  brandId?: string | number;

  /**
   * 分类id
   */
  catelogId?: string | number;

  /**
   *
   */
  brandName?: string;

  /**
   *
   */
  catelogName?: string;

}

export interface CategoryBrandRelationQuery extends PageQuery {

  /**
   * 品牌id
   */
  brandId?: string | number;

  /**
   * 日期范围参数
   */
  params?: any;
}
