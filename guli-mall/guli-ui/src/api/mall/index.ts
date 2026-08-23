import request from '@/utils/request'
import type { ESearchParam, ESearchListVo, PmsSkuItemVo } from './types'

/** 商品检索 */
export function esearch(data: ESearchParam) {
  return request<ESearchListVo, ESearchListVo>({
    url: '/display/esearch',
    method: 'post',
    data
  })
}

/** SKU 详情 */
export function item(spuId: number) {
  return request<PmsSkuItemVo, PmsSkuItemVo>({
    url: `/display/item/${spuId}`,
    method: 'get'
  })
}
