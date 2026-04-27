import { get } from '../utils/request.js'

/**
 * 获取公告列表
 */
export function getAnnouncements(params = {}) {
  return get('/owner/announcements', params)
}

/**
 * 获取公告详情
 */
export function getAnnouncementDetail(id) {
  return get(`/owner/announcements/${id}`)
}

/**
 * 标记公告已读
 */
export function markAsRead(id) {
  return get(`/owner/announcements/${id}/read`)
}

/**
 * 获取最新公告（首页用）
 */
export function getLatestAnnouncements(limit = 5) {
  return get('/owner/announcements/latest', { limit })
}
