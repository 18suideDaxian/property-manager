import request from '@/utils/request'

// 获取报修工单列表
export function getRepairList(params) {
  return request({
    url: '/admin/repairs',
    method: 'get',
    params
  })
}

// 获取报修工单详情
export function getRepairDetail(id) {
  return request({
    url: `/admin/repairs/${id}`,
    method: 'get'
  })
}

// 分配工单
export function assignRepair(id, data) {
  return request({
    url: `/admin/repairs/${id}/assign`,
    method: 'put',
    data
  })
}

// 处理工单
export function processRepair(id, data) {
  return request({
    url: `/admin/repairs/${id}/process`,
    method: 'put',
    data
  })
}

// 完成工单
export function completeRepair(id, data) {
  return request({
    url: `/admin/repairs/${id}/complete`,
    method: 'put',
    data
  })
}

// 关闭工单
export function closeRepair(id) {
  return request({
    url: `/admin/repairs/${id}/close`,
    method: 'put'
  })
}

// 删除工单
export function deleteRepair(id) {
  return request({
    url: `/admin/repairs/${id}`,
    method: 'delete'
  })
}
