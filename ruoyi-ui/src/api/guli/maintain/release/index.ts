import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import type { ReleaseForm } from '@/api/guli/maintain/release/types'
import type { CategoryVO } from '@/api/guli/category/types'
import type { CategoryBrandRelationVO } from '@/api/guli/brand/types'
import type { AttrGroupWithAttrsVO } from '@/api/guli/maintain/release/types'

/** 新增spu信息 */
export const addRelease = (data: ReleaseForm) => {
  return request({ url: 'guli/maintain/release', method: 'post', data })
}

/** 获取分类树 */
export const listTreeCategory = (): AxiosPromise<CategoryVO[]> => {
  return request({ url: 'guli/maintain/release/treeCategory', method: 'get' })
}

/** 根据分类id查询品牌列表 */
export const listBrands = (catelogId: string | number): AxiosPromise<CategoryBrandRelationVO[]> => {
  return request({ url: 'guli/maintain/release/brands', method: 'get', params: { catelogId } })
}

/** 根据分类id查询属性分组及其属性列表 */
export const listAttrGroups = (catelogId: string | number): AxiosPromise<AttrGroupWithAttrsVO[]> => {
  return request({ url: 'guli/maintain/release/attrGroups', method: 'get', params: { catelogId } })
}
