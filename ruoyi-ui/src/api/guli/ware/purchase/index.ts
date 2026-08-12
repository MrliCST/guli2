import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import { PurchaseVO, PurchaseForm, PurchaseQuery } from '@/api/guli/ware/purchase/types'

/**
 * 查询采购信息列表
 * @param query
 * @returns {*}
 */

export const listPurchase = (query?: PurchaseQuery): AxiosPromise<PurchaseVO[]> => {
  return request({
    url: '/ware/purchase/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询采购信息详细
 * @param id
 */
export const getPurchase = (id: string | number): AxiosPromise<PurchaseVO> => {
  return request({
    url: '/ware/purchase/' + id,
    method: 'get'
  })
}

/**
 * 新增采购信息
 * @param data
 */
export const addPurchase = (data: PurchaseForm) => {
  return request({
    url: '/ware/purchase',
    method: 'post',
    data: data
  })
}

/**
 * 修改采购信息
 * @param data
 */
export const updatePurchase = (data: PurchaseForm) => {
  return request({
    url: '/ware/purchase',
    method: 'put',
    data: data
  })
}

/**
 * 删除采购信息
 * @param id
 */
export const delPurchase = (id: string | number | Array<string | number>) => {
  return request({
    url: '/ware/purchase/' + id,
    method: 'delete'
  })
}
