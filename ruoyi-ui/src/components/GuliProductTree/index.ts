import request from '@/utils/request'
import type { AxiosPromise } from 'axios'

// ======================== 类型定义 ========================

export interface CategoryVO {
  catId: string | number
  name: string
  parentCid: string | number
  catLevel: number
  showStatus: number
  sort: number
  icon: string
  productUnit: string
  productCount: number
  children: CategoryVO[]
}

export interface CategoryForm {
  catId?: string | number
  name?: string
  parentCid?: string | number
  catLevel?: number
  showStatus?: number
  sort?: number
  icon?: string
  productUnit?: string
  productCount?: number
}

export interface CategoryQuery {
  catId?: string | number
  name?: string
  params?: any
}

// ======================== 暴露给父组件的 API ========================

export interface TreeApi {
  loadTree: () => Promise<void>
  getTreeData: () => CategoryVO[]
  getById: (catId: number) => Promise<CategoryVO>
  add: (form: CategoryForm) => Promise<void>
  update: (form: CategoryForm) => Promise<void>
  remove: (ids: number | number[]) => Promise<void>
  removeBatch: () => Promise<void>
}

// ======================== API 接口 ========================

/** 查询商品三级分类列表 */
export const listCategory = (query?: CategoryQuery): AxiosPromise<CategoryVO[]> => {
  return request({ url: '/guli/category/list', method: 'get', params: query })
}

/** 查询商品三级分类树列表 */
export const listTreeCategory = (): AxiosPromise<CategoryVO[]> => {
  return request({ url: '/guli/category/list/tree', method: 'get' })
}

/** 查询商品三级分类详细 */
export const getCategory = (catId: string | number): AxiosPromise<CategoryVO> => {
  return request({ url: '/guli/category/' + catId, method: 'get' })
}

/** 新增商品三级分类 */
export const addCategory = (data: CategoryForm) => {
  return request({ url: '/guli/category', method: 'post', data })
}

/** 修改商品三级分类 */
export const updateCategory = (data: CategoryForm) => {
  return request({ url: '/guli/category', method: 'put', data })
}

/** 批量修改商品三级分类 */
export const updateBatchCategory = (dataList: CategoryForm[]) => {
  return request({ url: '/guli/category/batch', method: 'put', data: dataList })
}

/** 删除商品三级分类 */
export const delCategory = (catId: string | number | Array<string | number>) => {
  return request({ url: '/guli/category/' + catId, method: 'delete' })
}
