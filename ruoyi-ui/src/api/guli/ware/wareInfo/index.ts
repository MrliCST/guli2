import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import { WareInfoVO, WareInfoForm, WareInfoQuery } from '@/api/guli/ware/wareInfo/types'

/**
 * 查询仓库信息列表
 * @param query
 * @returns {*}
 */

export const listWareInfo = (query?: WareInfoQuery): AxiosPromise<WareInfoVO[]> => {
  return request({
    url: '/ware/wareInfo/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询仓库信息详细
 * @param id
 */
export const getWareInfo = (id: string | number): AxiosPromise<WareInfoVO> => {
  return request({
    url: '/ware/wareInfo/' + id,
    method: 'get'
  })
}

/**
 * 新增仓库信息
 * @param data
 */
export const addWareInfo = (data: WareInfoForm) => {
  return request({
    url: '/ware/wareInfo',
    method: 'post',
    data: data
  })
}

/**
 * 修改仓库信息
 * @param data
 */
export const updateWareInfo = (data: WareInfoForm) => {
  return request({
    url: '/ware/wareInfo',
    method: 'put',
    data: data
  })
}

/**
 * 删除仓库信息
 * @param id
 */
export const delWareInfo = (id: string | number | Array<string | number>) => {
  return request({
    url: '/ware/wareInfo/' + id,
    method: 'delete'
  })
}
