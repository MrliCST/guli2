import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import { CategoryVO, CategoryForm, CategoryQuery } from '@/api/guli/category/types'

/**
 * 查询商品三级分类列表
 * @param query
 * @returns {*}
 */

export const listCategory = (query?: CategoryQuery): AxiosPromise<CategoryVO[]> => {
  return request({
    url: '/guli/category/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询商品三级分类树列表
 * @param query
 * @returns {*}
 */
export const listTreeCategory = (query?: CategoryQuery): AxiosPromise<CategoryVO[]> => {
  return request({
    url: '/guli/category/list/tree',
    method: 'get',
    params: query
  })
}

/**
 * 查询商品三级分类详细
 * @param catId
 */
export const getCategory = (catId: string | number): AxiosPromise<CategoryVO> => {
  return request({
    url: '/guli/category/' + catId,
    method: 'get'
  })
}

/**
 * 新增商品三级分类
 * @param data
 */
export const addCategory = (data: CategoryForm) => {
  return request({
    url: '/guli/category',
    method: 'post',
    data: data
  })
}

/**
 * 修改商品三级分类
 * @param data
 */
export const updateCategory = (data: CategoryForm) => {
  return request({
    url: '/guli/category',
    method: 'put',
    data: data
  })
}

/**
 * 批量修改商品三级分类
 * @param dataList
 */
export const updateBatchCategory = (dataList: CategoryForm[]) => {
  return request({
    url: '/guli/category/batch',
    method: 'put',
    data: dataList
  })
}

/**
 * 删除商品三级分类
 * @param catId
 */
export const delCategory = (catId: string | number | Array<string | number>) => {
  return request({
    url: '/guli/category/' + catId,
    method: 'delete'
  })
}
