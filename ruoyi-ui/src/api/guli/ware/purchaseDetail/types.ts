export interface PurchaseDetailVO {
  /**
   *
   */
  id: string | number

  /**
   * 采购单id
   */
  purchaseId: string | number

  /**
   * 采购商品id
   */
  skuId: string | number

  /**
   * 采购数量
   */
  skuNum: number

  /**
   * 采购金额
   */
  skuPrice: number

  /**
   * 仓库id
   */
  wareId: string | number

  /**
   * 状态[0新建，1已分配，2正在采购，3已完成，4采购失败]
   */
  status: number
}

export interface PurchaseDetailForm extends BaseEntity {
  /**
   *
   */
  id?: string | number

  /**
   * 采购单id
   */
  purchaseId?: string | number

  /**
   * 采购商品id
   */
  skuId?: string | number

  /**
   * 采购数量
   */
  skuNum?: number

  /**
   * 采购金额
   */
  skuPrice?: number

  /**
   * 仓库id
   */
  wareId?: string | number

  /**
   * 状态[0新建，1已分配，2正在采购，3已完成，4采购失败]
   */
  status?: number
}

export interface PurchaseDetailQuery extends PageQuery {
  /**
   * 采购单id
   */
  purchaseId?: string | number

  /**
   * 采购商品id
   */
  skuId?: string | number

  /**
   * 日期范围参数
   */
  params?: any
}
