import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import { PurchaseDetailVO, PurchaseDetailForm, PurchaseDetailQuery } from '@/api/guli/ware/purchaseDetail/types'

/**
 * 查询采购单详情列表
 * @param query
 * @returns {*}
 */

export const listPurchaseDetail = (query?: PurchaseDetailQuery): AxiosPromise<PurchaseDetailVO[]> => {
  return request({
    url: '/ware/purchaseDetail/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询采购单详情详细
 * @param id
 */
export const getPurchaseDetail = (id: string | number): AxiosPromise<PurchaseDetailVO> => {
  return request({
    url: '/ware/purchaseDetail/' + id,
    method: 'get'
  })
}

/**
 * 新增采购单详情
 * @param data
 */
export const addPurchaseDetail = (data: PurchaseDetailForm) => {
  return request({
    url: '/ware/purchaseDetail',
    method: 'post',
    data: data
  })
}

/**
 * 修改采购单详情
 * @param data
 */
export const updatePurchaseDetail = (data: PurchaseDetailForm) => {
  return request({
    url: '/ware/purchaseDetail',
    method: 'put',
    data: data
  })
}

/**
 * 删除采购单详情
 * @param id
 */
export const delPurchaseDetail = (id: string | number | Array<string | number>) => {
  return request({
    url: '/ware/purchaseDetail/' + id,
    method: 'delete'
  })
}
