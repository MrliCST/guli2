export interface PurchaseVO {
  /**
   *
   */
  id: string | number

  /**
   *
   */
  assigneeId: string | number

  /**
   *
   */
  assigneeName: string

  /**
   *
   */
  phone: string

  /**
   *
   */
  priority: number

  /**
   *
   */
  status: number

  /**
   *
   */
  wareId: string | number

  /**
   *
   */
  amount: number
}

export interface PurchaseForm extends BaseEntity {
  /**
   *
   */
  id?: string | number

  /**
   *
   */
  assigneeId?: string | number

  /**
   *
   */
  assigneeName?: string

  /**
   *
   */
  phone?: string

  /**
   *
   */
  priority?: number

  /**
   *
   */
  status?: number

  /**
   *
   */
  wareId?: string | number

  /**
   *
   */
  amount?: number
}

export interface PurchaseQuery extends PageQuery {
  /**
   *
   */
  assigneeName?: string

  /**
   * 日期范围参数
   */
  params?: any
}
