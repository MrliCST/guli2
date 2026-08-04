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
