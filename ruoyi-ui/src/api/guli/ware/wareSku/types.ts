export interface WareSkuVO {
  /**
   * id
   */
  id: string | number

  /**
   * sku_id
   */
  skuId: string | number

  /**
   * 仓库id
   */
  wareId: string | number

  /**
   * 库存数
   */
  stock: number

  /**
   * sku_name
   */
  skuName: string

  /**
   * 锁定库存
   */
  stockLocked: number
}

export interface WareSkuForm extends BaseEntity {
  /**
   * id
   */
  id?: string | number

  /**
   * sku_id
   */
  skuId?: string | number

  /**
   * 仓库id
   */
  wareId?: string | number

  /**
   * 库存数
   */
  stock?: number

  /**
   * sku_name
   */
  skuName?: string

  /**
   * 锁定库存
   */
  stockLocked?: number
}

export interface WareSkuQuery extends PageQuery {
  /**
   * sku_id
   */
  skuId?: string | number

  /**
   * 仓库id
   */
  wareId?: string | number

  /**
   * 日期范围参数
   */
  params?: any
}
