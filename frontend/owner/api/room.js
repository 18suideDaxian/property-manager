import { get } from '../utils/request.js'

/**
 * 获取我的房产列表
 */
export function getMyRooms() {
  return get('/owner/rooms')
}

/**
 * 获取房产详情
 */
export function getRoomDetail(roomId) {
  return get(`/owner/rooms/${roomId}`)
}

/**
 * 绑定房产
 */
export function bindRoom(data) {
  return get('/owner/rooms/bind', data)
}

/**
 * 获取房产下的缴费记录
 */
export function getRoomBills(roomId, params = {}) {
  return get(`/owner/rooms/${roomId}/bills`, params)
}
