import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import { AttrGroupVO, AttrGroupForm, AttrGroupQuery } from '@/api/guli/attrGroup/types'

const BASE = '/guli/platformAttr/attrGroup'

/** 查询属性分组列表 */
export const listAttrGroup = (query?: AttrGroupQuery): AxiosPromise<AttrGroupVO[]> => {
  return request({ url: BASE + '/list', method: 'get', params: query })
}

/** 查询属性分组详细 */
export const getAttrGroup = (attrGroupId: string | number): AxiosPromise<AttrGroupVO> => {
  return request({ url: BASE + '/' + attrGroupId, method: 'get' })
}

/** 新增属性分组 */
export const addAttrGroup = (data: AttrGroupForm) => {
  return request({ url: BASE, method: 'post', data })
}

/** 修改属性分组 */
export const updateAttrGroup = (data: AttrGroupForm) => {
  return request({ url: BASE, method: 'put', data })
}

/** 删除属性分组 */
export const delAttrGroup = (attrGroupId: string | number | Array<string | number>) => {
  return request({ url: BASE + '/' + attrGroupId, method: 'delete' })
}
