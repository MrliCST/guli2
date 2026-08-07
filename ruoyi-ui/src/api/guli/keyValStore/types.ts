export interface KeyValStoreVO {
  /**
   * 属性id
   */
  attrId: string | number;

  /**
   * 属性名
   */
  attrName: string;

  /**
   * 是否需要检索[0-不需要，1-需要]
   */
  searchType: number;

  /**
   * 值类型[0-为单个值，1-可以选择多个值]
   */
  valueType: number;

  /**
   * 属性图标
   */
  icon: string;

  /**
   * 可选值列表[用逗号分隔]
   */
  valueSelect: string;

  /**
   * 属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]
   */
  attrType: number;

  /**
   * 启用状态[0 - 禁用，1 - 启用]
   */
  enable: number;

  /**
   * 所属分类
   */
  catelogId: string | number;

  /**
   * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
   */
  showDesc: number;

}

export interface KeyValStoreForm extends BaseEntity {
  /**
   * 属性id
   */
  attrId?: string | number;

  /**
   * 属性名
   */
  attrName?: string;

  /**
   * 是否需要检索[0-不需要，1-需要]
   */
  searchType?: number;

  /**
   * 值类型[0-为单个值，1-可以选择多个值]
   */
  valueType?: number;

  /**
   * 属性图标
   */
  icon?: string;

  /**
   * 可选值列表[用逗号分隔]
   */
  valueSelect?: string;

  /**
   * 属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]
   */
  attrType?: number;

  /**
   * 启用状态[0 - 禁用，1 - 启用]
   */
  enable?: number;

  /**
   * 所属分类
   */
  catelogId?: string | number;

  /**
   * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
   */
  showDesc?: number;

}

export interface KeyValStoreQuery extends PageQuery {

  /**
   * 所属分类id
   */
  catelogId?: string | number;

  /**
   * 属性名
   */
  attrName?: string;

  /**
   * 值类型[0-为单个值，1-可以选择多个值]
   */
  valueType?: number;

  /**
   * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
   */
  showDesc?: number;

  /**
   * 日期范围参数
   */
  params?: any;
}



