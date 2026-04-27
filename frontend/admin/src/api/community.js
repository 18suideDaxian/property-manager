import request from '@/utils/request'

// 获取小区列表
export function getCommunityList(params) {
  return request({
    url: '/admin/communities',
    method: 'get',
    params
  })
}

// 获取所有小区（下拉选择用）
export function getAllCommunities() {
  return request({
    url: '/admin/communities/all',
    method: 'get'
  })
}

// 获取小区详情
export function getCommunityDetail(id) {
  return request({
    url: `/admin/communities/${id}`,
    method: 'get'
  })
}

// 创建小区
export function createCommunity(data) {
  return request({
    url: '/admin/communities',
    method: 'post',
    data
  })
}

// 更新小区
export function updateCommunity(id, data) {
  return request({
    url: `/admin/communities/${id}`,
    method: 'put',
    data
  })
}

// 删除小区
export function deleteCommunity(id) {
  return request({
    url: `/admin/communities/${id}`,
    method: 'delete'
  })
}
