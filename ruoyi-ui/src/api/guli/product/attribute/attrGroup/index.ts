import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import type { CategoryVO } from '@/api/guli/product/category/types'
import type { KeyValStoreVO } from '@/api/guli/product/attribute/keyValStore/types'
import {
  AttrGroupVO,
  AttrGroupForm,
  AttrGroupQuery,
  AttrAttrgroupRelationVO,
  AttrAttrgroupRelationForm,
  AttrAttrgroupRelationQuery
} from '@/api/guli/product/attribute/attrGroup/types'

// ======================== 属性分组 CRUD ========================

/** 获取分类树数据 */
export const listTreeCategory = (): AxiosPromise<CategoryVO[]> => {
  return request({ url: 'product/attribute/attrGroup/treeCategory', method: 'get' })
}

/** 查询属性分组列表 */
export const listAttrGroup = (query?: AttrGroupQuery): AxiosPromise<AttrGroupVO[]> => {
  return request({ url: 'product/attribute/attrGroup/list', method: 'get', params: query })
}

/** 查询属性分组详细 */
export const getAttrGroup = (attrGroupId: string | number): AxiosPromise<AttrGroupVO> => {
  return request({ url: 'product/attribute/attrGroup/' + attrGroupId, method: 'get' })
}

/** 新增属性分组 */
export const addAttrGroup = (data: AttrGroupForm) => {
  return request({ url: 'product/attribute/attrGroup', method: 'post', data })
}

/** 修改属性分组 */
export const updateAttrGroup = (data: AttrGroupForm) => {
  return request({ url: 'product/attribute/attrGroup', method: 'put', data })
}

/** 删除属性分组 */
export const delAttrGroup = (attrGroupId: string | number | Array<string | number>) => {
  return request({ url: 'product/attribute/attrGroup/' + attrGroupId, method: 'delete' })
}

// ======================== 属性分组-属性值储关联 ========================

/** 查询可关联的属性列表（过滤已关联的），返回属性值储 VO */
export const listAvailableAttrs = (catelogId: string | number): AxiosPromise<KeyValStoreVO[]> => {
  return request({ url: 'product/attribute/attrGroup/availableAttrs', method: 'get', params: { catelogId } })
}

/** 查询关联列表（附带属性名和分组名） */
export const listRelations = (query?: AttrAttrgroupRelationQuery): AxiosPromise<AttrAttrgroupRelationVO[]> => {
  return request({ url: 'product/attribute/attrGroup/relations', method: 'get', params: query })
}

/** 新增关联 */
export const addRelation = (data: AttrAttrgroupRelationForm) => {
  return request({ url: 'product/attribute/attrGroup/relation', method: 'post', data })
}

/** 修改关联（仅排序） */
export const updateRelation = (data: AttrAttrgroupRelationForm) => {
  return request({ url: 'product/attribute/attrGroup/relation', method: 'put', data })
}

/** 删除关联 */
export const delRelation = (id: string | number | Array<string | number>) => {
  return request({ url: 'product/attribute/attrGroup/relation/' + id, method: 'delete' })
}
