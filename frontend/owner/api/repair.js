import { get, post, upload } from '../utils/request.js'

/**
 * 提交报修
 */
export function submitRepair(data) {
  return post('/owner/repairs', data)
}

/**
 * 获取我的报修列表
 */
export function getMyRepairs(params = {}) {
  return get('/owner/repairs', params)
}

/**
 * 获取报修详情
 */
export function getRepairDetail(repairId) {
  return get(`/owner/repairs/${repairId}`)
}

/**
 * 取消报修
 */
export function cancelRepair(repairId) {
  return post(`/owner/repairs/${repairId}/cancel`)
}

/**
 * 评价报修
 */
export function rateRepair(repairId, data) {
  return post(`/owner/repairs/${repairId}/rate`, data)
}

/**
 * 上传报修图片
 */
export function uploadRepairImage(filePath) {
  return upload(filePath, { url: '/file/upload/repair' })
}
