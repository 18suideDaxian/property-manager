import config from './config.js'
import { getToken, clearAuth } from './auth.js'

/**
 * 封装 uni.request
 * @param {Object} options - 请求配置
 * @param {string} options.url - 请求路径（相对路径）
 * @param {string} options.method - 请求方法
 * @param {Object} options.data - 请求数据
 * @param {Object} options.header - 额外请求头
 * @param {boolean} options.showLoading - 是否显示加载中
 * @param {string} options.loadingText - 加载提示文字
 * @param {boolean} options.showError - 是否显示错误提示
 */
function request(options = {}) {
  const {
    url,
    method = 'GET',
    data = {},
    header = {},
    showLoading = false,
    loadingText = '加载中...',
    showError = true
  } = options

  if (showLoading) {
    uni.showLoading({ title: loadingText, mask: true })
  }

  const token = getToken()
  const requestHeader = {
    'Content-Type': 'application/json',
    ...header
  }
  if (token) {
    requestHeader['Authorization'] = 'Bearer ' + token
  }

  return new Promise((resolve, reject) => {
    uni.request({
      url: config.BASE_URL + config.API_PREFIX + url,
      method: method,
      data: data,
      header: requestHeader,
      timeout: config.TIMEOUT,
      success: (res) => {
        if (showLoading) uni.hideLoading()

        const statusCode = res.statusCode
        const responseData = res.data

        if (statusCode === 200) {
          // 后端返回格式: { code: 200, message: "success", data: {...} }
          if (responseData.code === 200 || responseData.code === 0) {
            resolve(responseData.data !== undefined ? responseData.data : responseData)
          } else {
            const errMsg = responseData.message || '请求失败'
            if (showError) {
              uni.showToast({ title: errMsg, icon: 'none', duration: 2000 })
            }
            reject(responseData)
          }
        } else if (statusCode === 401) {
          // 未授权，清除登录态并跳转登录
          clearAuth()
          uni.showToast({ title: '登录已过期，请重新登录', icon: 'none', duration: 2000 })
          setTimeout(() => {
            uni.reLaunch({ url: '/pages/login/login' })
          }, 1500)
          reject({ code: 401, message: '未授权' })
        } else if (statusCode === 403) {
          uni.showToast({ title: '没有访问权限', icon: 'none' })
          reject({ code: 403, message: '没有访问权限' })
        } else {
          const errMsg = responseData.message || `请求错误(${statusCode})`
          if (showError) {
            uni.showToast({ title: errMsg, icon: 'none', duration: 2000 })
          }
          reject({ code: statusCode, message: errMsg })
        }
      },
      fail: (err) => {
        if (showLoading) uni.hideLoading()
        const errMsg = '网络异常，请检查网络连接'
        if (showError) {
          uni.showToast({ title: errMsg, icon: 'none', duration: 2000 })
        }
        reject({ code: -1, message: errMsg, detail: err })
      }
    })
  })
}

/**
 * GET 请求
 */
export function get(url, data = {}, options = {}) {
  return request({ url, method: 'GET', data, ...options })
}

/**
 * POST 请求
 */
export function post(url, data = {}, options = {}) {
  return request({ url, method: 'POST', data, ...options })
}

/**
 * PUT 请求
 */
export function put(url, data = {}, options = {}) {
  return request({ url, method: 'PUT', data, ...options })
}

/**
 * DELETE 请求
 */
export function del(url, data = {}, options = {}) {
  return request({ url, method: 'DELETE', data, ...options })
}

/**
 * 上传文件
 */
export function upload(filePath, options = {}) {
  const { url = '/file/upload', name = 'file', formData = {}, showLoading = true } = options
  const token = getToken()

  if (showLoading) {
    uni.showLoading({ title: '上传中...', mask: true })
  }

  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: config.BASE_URL + config.API_PREFIX + url,
      filePath: filePath,
      name: name,
      formData: formData,
      header: {
        'Authorization': token ? 'Bearer ' + token : ''
      },
      success: (res) => {
        if (showLoading) uni.hideLoading()
        if (res.statusCode === 200) {
          const data = JSON.parse(res.data)
          if (data.code === 200 || data.code === 0) {
            resolve(data.data || data)
          } else {
            uni.showToast({ title: data.message || '上传失败', icon: 'none' })
            reject(data)
          }
        } else {
          uni.showToast({ title: '上传失败', icon: 'none' })
          reject({ code: res.statusCode, message: '上传失败' })
        }
      },
      fail: (err) => {
        if (showLoading) uni.hideLoading()
        uni.showToast({ title: '上传失败', icon: 'none' })
        reject(err)
      }
    })
  })
}

export default { request, get, post, put, del, upload }
