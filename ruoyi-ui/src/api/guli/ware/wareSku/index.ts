import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import { WareSkuVO, WareSkuForm, WareSkuQuery } from '@/api/guli/ware/wareSku/types'

/**
 * 查询商品库存列表
 * @param query
 * @returns {*}
 */

export const listWareSku = (query?: WareSkuQuery): AxiosPromise<WareSkuVO[]> => {
  return request({
    url: '/ware/wareSku/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询商品库存详细
 * @param id
 */
export const getWareSku = (id: string | number): AxiosPromise<WareSkuVO> => {
  return request({
    url: '/ware/wareSku/' + id,
    method: 'get'
  })
}

/**
 * 新增商品库存
 * @param data
 */
export const addWareSku = (data: WareSkuForm) => {
  return request({
    url: '/ware/wareSku',
    method: 'post',
    data: data
  })
}

/**
 * 修改商品库存
 * @param data
 */
export const updateWareSku = (data: WareSkuForm) => {
  return request({
    url: '/ware/wareSku',
    method: 'put',
    data: data
  })
}

/**
 * 删除商品库存
 * @param id
 */
export const delWareSku = (id: string | number | Array<string | number>) => {
  return request({
    url: '/ware/wareSku/' + id,
    method: 'delete'
  })
}
