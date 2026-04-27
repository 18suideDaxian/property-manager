// API 基础地址配置
// #ifdef MP-WEIXIN
const BASE_URL = 'http://localhost:8080'
// #endif
// #ifndef MP-WEIXIN
const BASE_URL = 'http://localhost:8080'
// #endif

export default {
  BASE_URL,
  // API 路径前缀
  API_PREFIX: '/api',
  // 超时时间（毫秒）
  TIMEOUT: 15000,
  // 文件上传地址
  UPLOAD_URL: BASE_URL + '/api/file/upload'
}
