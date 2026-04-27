import { get, post } from '../utils/request.js'

/**
 * 获取我的账单列表
 */
export function getMyBills(params = {}) {
  return get('/owner/bills', params)
}

/**
 * 获取账单详情
 */
export function getBillDetail(billId) {
  return get(`/owner/bills/${billId}`)
}

/**
 * 缴费（发起支付）
 */
export function payBill(billId, payMethod) {
  return post(`/owner/bills/${billId}/pay`, { payMethod })
}

/**
 * 查询缴费状态
 */
export function queryPayStatus(billId) {
  return get(`/owner/bills/${billId}/pay-status`)
}

/**
 * 获取待缴费金额汇总
 */
export function getUnpaidSummary() {
  return get('/owner/bills/unpaid-summary')
}
