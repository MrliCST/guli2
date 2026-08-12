export interface WareInfoVO {
  /**
   * id
   */
  id: string | number

  /**
   * 仓库名
   */
  name: string

  /**
   * 仓库地址
   */
  address: string

  /**
   * 区域编码
   */
  areacode: string
}

export interface WareInfoForm extends BaseEntity {
  /**
   * id
   */
  id?: string | number

  /**
   * 仓库名
   */
  name?: string

  /**
   * 仓库地址
   */
  address?: string

  /**
   * 区域编码
   */
  areacode?: string
}

export interface WareInfoQuery extends PageQuery {
  /**
   * 仓库名
   */
  name?: string

  /**
   * 仓库地址
   */
  address?: string

  /**
   * 区域编码
   */
  areacode?: string

  /**
   * 日期范围参数
   */
  params?: any
}
