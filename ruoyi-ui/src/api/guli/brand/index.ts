import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import {
  BrandVO,
  BrandForm,
  BrandQuery,
  CategoryBrandRelationVO,
  CategoryBrandRelationForm,
  CategoryBrandRelationQuery
} from '@/api/guli/brand/types'

// =========  品牌  =========

const BRAND_BASE = '/guli/brand'

/**
 * 查询品牌列表
 * @param query
 * @returns {*}
 */
export const listBrand = (query?: BrandQuery): AxiosPromise<BrandVO[]> => {
  return request({ url: BRAND_BASE + '/list', method: 'get', params: query })
}

/**
 * 查询品牌详细
 * @param brandId
 */
export const getBrand = (brandId: string | number): AxiosPromise<BrandVO> => {
  return request({ url: BRAND_BASE + '/' + brandId, method: 'get' })
}

/**
 * 新增品牌
 * @param data
 */
export const addBrand = (data: BrandForm) => {
  return request({ url: BRAND_BASE, method: 'post', data })
}

/**
 * 修改品牌
 * @param data
 */
export const updateBrand = (data: BrandForm) => {
  return request({ url: BRAND_BASE, method: 'put', data })
}

/**
 * 删除品牌
 * @param brandId
 */
export const delBrand = (brandId: string | number | Array<string | number>) => {
  return request({ url: BRAND_BASE + '/' + brandId, method: 'delete' })
}

// =========  品牌分类关联  =========

const CBR_BASE = '/guli/brand/cbr'

/**
 * 查询品牌分类关联列表
 * @param query
 * @returns {*}
 */
export const listCbr = (query?: CategoryBrandRelationQuery): AxiosPromise<CategoryBrandRelationVO[]> => {
  return request({ url: CBR_BASE + 'List', method: 'get', params: query })
}

/**
 * 查询品牌分类关联详细
 * @param cbrId
 */
export const getCbr = (cbrId: string | number): AxiosPromise<CategoryBrandRelationVO> => {
  return request({ url: CBR_BASE + '/' + cbrId, method: 'get' })
}

/**
 * 新增品牌分类关联
 * @param data
 */
export const addCbr = (data: CategoryBrandRelationForm) => {
  return request({ url: CBR_BASE, method: 'post', data })
}

/**
 * 修改品牌分类关联
 * @param data
 */
export const updateCbr = (data: CategoryBrandRelationForm) => {
  return request({ url: CBR_BASE, method: 'put', data })
}

/**
 * 删除品牌分类关联
 * @param cbrId
 */
export const delCbr = (cbrId: string | number | Array<string | number>) => {
  return request({ url: CBR_BASE + '/' + cbrId, method: 'delete' })
}
