export interface AttrGroupVO {
  attrGroupId: string | number
  attrGroupName: string
  sort: number
  descript: string
  icon: string
  catelogId: string | number
}

export interface AttrGroupForm extends BaseEntity {
  attrGroupId?: string | number
  attrGroupName?: string
  sort?: number
  descript?: string
  icon?: string
  catelogId?: string | number
}

export interface AttrGroupQuery extends PageQuery {
  catelogId?: string | number
  attrGroupName?: string
  params?: any
}

// ======================== 属性分组-属性值储关联 ========================

export interface AttrAttrgroupRelationVO {
  /**
   * id
   */
  id: string | number

  /**
   * 属性id
   */
  attrId: string | number

  /**
   * 属性分组id
   */
  attrGroupId: string | number

  /**
   * 属性组内排序
   */
  attrSort: number

  /**
   * 属性名（联表 pms_attr）
   */
  attrName: string

  /**
   * 属性分组名（联表 pms_attr_group）
   */
  attrGroupName: string
}

export interface AttrAttrgroupRelationForm extends BaseEntity {
  /**
   * id
   */
  id?: string | number

  /**
   * 属性id
   */
  attrId?: string | number

  /**
   * 属性分组id
   */
  attrGroupId?: string | number

  /**
   * 属性组内排序
   */
  attrSort?: number
}

export interface AttrAttrgroupRelationQuery extends PageQuery {
  attrGroupId?: string | number
  params?: any
}
