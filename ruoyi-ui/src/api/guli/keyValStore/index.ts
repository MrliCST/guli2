import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { AttrKeyValueVO, AttrKeyValueForm, AttrKeyValueQuery } from '@/api/guli/attrKeyValue/types';

/**
 * 查询商品属性列表
 * @param query
 * @returns {*}
 */

export const listAttrKeyValue = (query?: AttrKeyValueQuery): AxiosPromise<AttrKeyValueVO[]> => {
  return request({
    url: '/guli/platformAttr/attrKeyValue/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询商品属性详细
 * @param attrId
 */
export const getAttrKeyValue = (attrId: string | number): AxiosPromise<AttrKeyValueVO> => {
  return request({
    url: '/guli/platformAttr/attrKeyValue/' + attrId,
    method: 'get'
  });
};

/**
 * 新增商品属性
 * @param data
 */
export const addAttrKeyValue = (data: AttrKeyValueForm) => {
  return request({
    url: '/guli/platformAttr/attrKeyValue',
    method: 'post',
    data: data
  });
};

/**
 * 修改商品属性
 * @param data
 */
export const updateAttrKeyValue = (data: AttrKeyValueForm) => {
  return request({
    url: '/guli/platformAttr/attrKeyValue',
    method: 'put',
    data: data
  });
};

/**
 * 删除商品属性
 * @param attrId
 */
export const delAttrKeyValue = (attrId: string | number | Array<string | number>) => {
  return request({
    url: '/guli/platformAttr/attrKeyValue/' + attrId,
    method: 'delete'
  });
};
