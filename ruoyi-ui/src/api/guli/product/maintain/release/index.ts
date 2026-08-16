import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import type { ReleaseForm } from '@/api/guli/product/maintain/release/types'
import type { CategoryVO } from '@/api/guli/product/category/types'
import type { CategoryBrandRelationVO } from '@/api/guli/product/brand/types'
import type { AttrGroupWithAttrsVO, AttrVO } from '@/api/guli/product/maintain/release/types'

/** 新增spu信息 */
export const addRelease = (data: ReleaseForm) => {
  return request({ url: 'product/maintain/release', method: 'post', data })
}

/** 获取分类树 */
export const listTreeCategory = (): AxiosPromise<CategoryVO[]> => {
  return request({ url: 'product/maintain/treeCategory', method: 'get' })
}

/** 根据分类id查询品牌列表 */
export const listBrands = (catelogId: string | number): AxiosPromise<CategoryBrandRelationVO[]> => {
  return request({ url: 'product/maintain/brands', method: 'get', params: { catelogId } })
}

/** 根据分类id查询属性分组及其属性列表 */
export const listAttrGroups = (catelogId: string | number): AxiosPromise<AttrGroupWithAttrsVO[]> => {
  return request({ url: 'product/maintain/attrGroups', method: 'get', params: { catelogId } })
}

/** 根据分类id查询销售属性列表 */
export const listSaleAttrs = (catelogId: string | number): AxiosPromise<AttrVO[]> => {
  return request({ url: 'product/maintain/saleAttrs', method: 'get', params: { catelogId } })
}
