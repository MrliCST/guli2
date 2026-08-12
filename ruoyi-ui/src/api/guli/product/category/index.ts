import request from '@/utils/request'
import { AxiosPromise } from 'axios'
import { CategoryVO, CategoryForm, CategoryQuery } from '@/api/guli/product/category/types'

/** 查询商品三级分类树列表 */
export const listTreeCategory = (query?: CategoryQuery): AxiosPromise<CategoryVO[]> => {
  return request({ url: 'product/category/listTree', method: 'get', params: query })
}

/** 查询商品三级分类详细 */
export const getCategory = (catId: string | number): AxiosPromise<CategoryVO> => {
  return request({ url: 'product/category/' + catId, method: 'get' })
}

/** 新增商品三级分类 */
export const addCategory = (data: CategoryForm) => {
  return request({ url: 'product/category', method: 'post', data })
}

/** 修改商品三级分类 */
export const updateCategory = (data: CategoryForm) => {
  return request({ url: 'product/category', method: 'put', data })
}

/** 批量修改商品三级分类 */
export const updateBatchCategory = (dataList: CategoryForm[]) => {
  return request({ url: 'product/category/batch', method: 'put', data: dataList })
}

/** 删除商品三级分类 */
export const delCategory = (catId: string | number | Array<string | number>) => {
  return request({ url: 'product/category/' + catId, method: 'delete' })
}
