export interface CategoryVO {
  /**
   * 分类id
   */
  catId: string | number

  /**
   * 分类名称
   */
  name: string

  /**
   * 父分类id
   */
  parentCid: string | number

  /**
   * 层级
   */
  catLevel: number

  /**
   * 是否显示[0-不显示，1显示]
   */
  showStatus: number

  /**
   * 排序
   */
  sort: number

  /**
   * 图标地址
   */
  icon: string

  /**
   * 计量单位
   */
  productUnit: string

  /**
   * 商品数量
   */
  productCount: number

  /**
   * 子分类
   */
  children: CategoryVO[]
}

export interface CategoryForm extends BaseEntity {
  /**
   * 分类id
   */
  catId?: string | number

  /**
   * 分类名称
   */
  name?: string

  /**
   * 父分类id
   */
  parentCid?: string | number

  /**
   * 层级
   */
  catLevel?: number

  /**
   * 是否显示[0-不显示，1显示]
   */
  showStatus?: number

  /**
   * 排序
   */
  sort?: number

  /**
   * 图标地址
   */
  icon?: string

  /**
   * 计量单位
   */
  productUnit?: string

  /**
   * 商品数量
   */
  productCount?: number
}

export interface CategoryQuery extends PageQuery {
  /**
   * 分类id
   */
  catId?: string | number

  /**
   * 分类名称
   */
  name?: string

  /**
   * 日期范围参数
   */
  params?: any
}
