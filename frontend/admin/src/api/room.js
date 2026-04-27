import request from '@/utils/request'

// 获取房产列表
export function getRoomList(params) {
  return request({
    url: '/admin/rooms',
    method: 'get',
    params
  })
}

// 获取房产详情
export function getRoomDetail(id) {
  return request({
    url: `/admin/rooms/${id}`,
    method: 'get'
  })
}

// 创建房产
export function createRoom(data) {
  return request({
    url: '/admin/rooms',
    method: 'post',
    data
  })
}

// 更新房产
export function updateRoom(id, data) {
  return request({
    url: `/admin/rooms/${id}`,
    method: 'put',
    data
  })
}

// 删除房产
export function deleteRoom(id) {
  return request({
    url: `/admin/rooms/${id}`,
    method: 'delete'
  })
}

// Excel 导入房产
export function importRooms(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/admin/rooms/import',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// Excel 导出房产
export function exportRooms(params) {
  return request({
    url: '/admin/rooms/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 下载导入模板
export function downloadTemplate() {
  return request({
    url: '/admin/rooms/template',
    method: 'get',
    responseType: 'blob'
  })
}
