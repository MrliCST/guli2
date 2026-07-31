import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import { BrandVO, BrandForm, BrandQuery } from '@/api/guli/brand/types'

/**
 * 查询品牌列表
 * @param query
 * @returns {*}
 */

export const listBrand = (query?: BrandQuery): AxiosPromise<BrandVO[]> => {
  return request({
    url: '/guli/brand/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询品牌详细
 * @param brandId
 */
export const getBrand = (brandId: string | number): AxiosPromise<BrandVO> => {
  return request({
    url: '/guli/brand/' + brandId,
    method: 'get'
  })
}

/**
 * 新增品牌
 * @param data
 */
export const addBrand = (data: BrandForm) => {
  return request({
    url: '/guli/brand',
    method: 'post',
    data: data
  })
}

/**
 * 修改品牌
 * @param data
 */
export const updateBrand = (data: BrandForm) => {
  return request({
    url: '/guli/brand',
    method: 'put',
    data: data
  })
}

/**
 * 删除品牌
 * @param brandId
 */
export const delBrand = (brandId: string | number | Array<string | number>) => {
  return request({
    url: '/guli/brand/' + brandId,
    method: 'delete'
  })
}
