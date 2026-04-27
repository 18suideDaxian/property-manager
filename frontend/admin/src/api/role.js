import request from '@/utils/request'

// 获取角色列表
export function getRoleList(params) {
  return request({
    url: '/admin/roles',
    method: 'get',
    params
  })
}

// 获取所有角色（下拉选择用）
export function getAllRoles() {
  return request({
    url: '/admin/roles/all',
    method: 'get'
  })
}

// 获取角色详情
export function getRoleDetail(id) {
  return request({
    url: `/admin/roles/${id}`,
    method: 'get'
  })
}

// 创建角色
export function createRole(data) {
  return request({
    url: '/admin/roles',
    method: 'post',
    data
  })
}

// 更新角色
export function updateRole(id, data) {
  return request({
    url: `/admin/roles/${id}`,
    method: 'put',
    data
  })
}

// 删除角色
export function deleteRole(id) {
  return request({
    url: `/admin/roles/${id}`,
    method: 'delete'
  })
}

// 获取权限树
export function getPermissionTree() {
  return request({
    url: '/admin/permissions/tree',
    method: 'get'
  })
}

// 更新角色权限
export function updateRolePermissions(id, data) {
  return request({
    url: `/admin/roles/${id}/permissions`,
    method: 'put',
    data
  })
}

// 获取操作日志
export function getOperationLogs(params) {
  return request({
    url: '/admin/logs',
    method: 'get',
    params
  })
}
