import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import type { CategoryVO } from '@/api/guli/category/types'
import { KeyValStoreVO, KeyValStoreForm, KeyValStoreQuery } from '@/api/guli/attribute/keyValStore/types'

/** 获取分类树数据 */
export const listTreeCategory = (): AxiosPromise<CategoryVO[]> => {
  return request({ url: 'guli/attribute/keyValStore/treeCategory', method: 'get' })
}

/**
 * 查询商品属性列表
 * @param query
 * @returns {*}
 */

export const listKeyValStore = (query?: KeyValStoreQuery): AxiosPromise<KeyValStoreVO[]> => {
  return request({
    url: 'guli/attribute/keyValStore/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询商品属性详细
 * @param attrId
 */
export const getKeyValStore = (attrId: string | number): AxiosPromise<KeyValStoreVO> => {
  return request({
    url: 'guli/attribute/keyValStore/' + attrId,
    method: 'get'
  })
}

/**
 * 新增商品属性
 * @param data
 */
export const addKeyValStore = (data: KeyValStoreForm) => {
  return request({
    url: 'guli/attribute/keyValStore',
    method: 'post',
    data: data
  })
}

/**
 * 修改商品属性
 * @param data
 */
export const updateKeyValStore = (data: KeyValStoreForm) => {
  return request({
    url: 'guli/attribute/keyValStore',
    method: 'put',
    data: data
  })
}

/**
 * 删除商品属性
 * @param attrId
 */
export const delKeyValStore = (attrId: string | number | Array<string | number>) => {
  return request({
    url: 'guli/attribute/keyValStore/' + attrId,
    method: 'delete'
  })
}
