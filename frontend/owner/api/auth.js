import { post, get } from '../utils/request.js'

/**
 * 发送短信验证码
 */
export function sendSmsCode(phone) {
  return post('/auth/sms/send', { phone })
}

/**
 * 手机号验证码登录
 */
export function loginByPhone(phone, code) {
  return post('/auth/login/phone', { phone, code })
}

/**
 * 手机号注册
 */
export function register(data) {
  return post('/auth/register', data)
}

/**
 * 获取当前用户信息
 */
export function getCurrentUser() {
  return get('/auth/current')
}

/**
 * 退出登录
 */
export function logout() {
  return post('/auth/logout')
}

/**
 * 刷新 Token
 */
export function refreshToken() {
  return post('/auth/refresh')
}
