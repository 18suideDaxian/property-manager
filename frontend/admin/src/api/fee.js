import request from '@/utils/request'

// 获取缴费列表
export function getFeeList(params) {
  return request({
    url: '/admin/fees',
    method: 'get',
    params
  })
}

// 获取缴费详情
export function getFeeDetail(id) {
  return request({
    url: `/admin/fees/${id}`,
    method: 'get'
  })
}

// 创建缴费记录
export function createFee(data) {
  return request({
    url: '/admin/fees',
    method: 'post',
    data
  })
}

// 更新缴费记录
export function updateFee(id, data) {
  return request({
    url: `/admin/fees/${id}`,
    method: 'put',
    data
  })
}

// 删除缴费记录
export function deleteFee(id) {
  return request({
    url: `/admin/fees/${id}`,
    method: 'delete'
  })
}

// 标记已缴费
export function markAsPaid(id) {
  return request({
    url: `/admin/fees/${id}/pay`,
    method: 'put'
  })
}

// 批量生成账单
export function batchGenerate(data) {
  return request({
    url: '/admin/fees/batch-generate',
    method: 'post',
    data
  })
}

// 导出缴费记录
export function exportFees(params) {
  return request({
    url: '/admin/fees/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
