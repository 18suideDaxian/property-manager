import { get, post } from '../utils/request.js'

/**
 * 获取个人资料
 */
export function getProfile() {
  return get('/owner/profile')
}

/**
 * 更新个人资料
 */
export function updateProfile(data) {
  return post('/owner/profile', data)
}

/**
 * 修改昵称
 */
export function updateName(name) {
  return post('/owner/profile/name', { name })
}

/**
 * 修改头像
 */
export function updateAvatar(avatarUrl) {
  return post('/owner/profile/avatar', { avatar: avatarUrl })
}

/**
 * 修改手机号
 */
export function updatePhone(phone, code) {
  return post('/owner/profile/phone', { phone, code })
}
