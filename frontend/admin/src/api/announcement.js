import request from '@/utils/request'

// 获取公告列表
export function getAnnouncementList(params) {
  return request({
    url: '/admin/announcements',
    method: 'get',
    params
  })
}

// 获取公告详情
export function getAnnouncementDetail(id) {
  return request({
    url: `/admin/announcements/${id}`,
    method: 'get'
  })
}

// 创建公告
export function createAnnouncement(data) {
  return request({
    url: '/admin/announcements',
    method: 'post',
    data
  })
}

// 更新公告
export function updateAnnouncement(id, data) {
  return request({
    url: `/admin/announcements/${id}`,
    method: 'put',
    data
  })
}

// 删除公告
export function deleteAnnouncement(id) {
  return request({
    url: `/admin/announcements/${id}`,
    method: 'delete'
  })
}

// 发布公告
export function publishAnnouncement(id) {
  return request({
    url: `/admin/announcements/${id}/publish`,
    method: 'put'
  })
}

// 撤回公告
export function withdrawAnnouncement(id) {
  return request({
    url: `/admin/announcements/${id}/withdraw`,
    method: 'put'
  })
}
