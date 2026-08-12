import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import type { CategoryVO } from '@/api/guli/product/category/types'
import {
  BrandVO,
  BrandForm,
  BrandQuery,
  CategoryBrandRelationVO,
  CategoryBrandRelationForm,
  CategoryBrandRelationQuery
} from '@/api/guli/product/brand/types'

// =========  品牌  =========

/** 获取分类树数据 */
export const listTreeCategory = (): AxiosPromise<CategoryVO[]> => {
  return request({ url: 'product/brand/treeCategory', method: 'get' })
}

/** 查询品牌列表 */
export const listBrand = (query?: BrandQuery): AxiosPromise<BrandVO[]> => {
  return request({ url: 'product/brand/list', method: 'get', params: query })
}

/** 查询品牌详细 */
export const getBrand = (brandId: string | number): AxiosPromise<BrandVO> => {
  return request({ url: 'product/brand/' + brandId, method: 'get' })
}

/** 新增品牌 */
export const addBrand = (data: BrandForm) => {
  return request({ url: 'product/brand', method: 'post', data })
}

/** 修改品牌 */
export const updateBrand = (data: BrandForm) => {
  return request({ url: 'product/brand', method: 'put', data })
}

/** 删除品牌 */
export const delBrand = (brandId: string | number | Array<string | number>) => {
  return request({ url: 'product/brand/' + brandId, method: 'delete' })
}

// =========  品牌分类关联  =========

/** 查询品牌分类关联列表 */
export const listCbr = (query?: CategoryBrandRelationQuery): AxiosPromise<CategoryBrandRelationVO[]> => {
  return request({ url: 'product/brand/cbrList', method: 'get', params: query })
}

/** 新增品牌分类关联 */
export const addCbr = (data: CategoryBrandRelationForm) => {
  return request({ url: 'product/brand/cbr', method: 'post', data })
}

/** 修改品牌分类关联 */
export const updateCbr = (data: CategoryBrandRelationForm) => {
  return request({ url: 'product/brand/cbr', method: 'put', data })
}

/** 删除品牌分类关联 */
export const delCbr = (cbrId: string | number | Array<string | number>) => {
  return request({ url: 'product/brand/cbr/' + cbrId, method: 'delete' })
}
