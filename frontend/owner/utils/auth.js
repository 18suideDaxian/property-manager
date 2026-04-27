// Token 管理工具
const TOKEN_KEY = 'owner_token'
const USER_INFO_KEY = 'owner_user_info'

/**
 * 获取 Token
 */
export function getToken() {
  return uni.getStorageSync(TOKEN_KEY) || ''
}

/**
 * 设置 Token
 */
export function setToken(token) {
  uni.setStorageSync(TOKEN_KEY, token)
}

/**
 * 移除 Token
 */
export function removeToken() {
  uni.removeStorageSync(TOKEN_KEY)
}

/**
 * 获取用户信息
 */
export function getUserInfo() {
  const info = uni.getStorageSync(USER_INFO_KEY)
  return info ? (typeof info === 'string' ? JSON.parse(info) : info) : null
}

/**
 * 设置用户信息
 */
export function setUserInfo(userInfo) {
  uni.setStorageSync(USER_INFO_KEY, JSON.stringify(userInfo))
}

/**
 * 移除用户信息
 */
export function removeUserInfo() {
  uni.removeStorageSync(USER_INFO_KEY)
}

/**
 * 清除所有登录态
 */
export function clearAuth() {
  removeToken()
  removeUserInfo()
}

/**
 * 是否已登录
 */
export function isLoggedIn() {
  return !!getToken()
}
