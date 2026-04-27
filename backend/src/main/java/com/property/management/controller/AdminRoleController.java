package com.property.management.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.*;
import com.property.management.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员-角色权限控制器
 */
@RestController
@RequestMapping("/admin/role")
@CrossOrigin
public class AdminRoleController {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    // ========== 角色管理 ==========

    /**
     * 查询角色列表
     */
    @GetMapping("/list")
    public ResponseResult<List<Role>> getRoleList(
            @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Role::getStatus, status);
        }
        wrapper.orderByAsc(Role::getSortOrder);
        return ResponseResult.success(roleMapper.selectList(wrapper));
    }

    /**
     * 创建角色
     */
    @PostMapping("")
    public ResponseResult<String> createRole(@RequestBody Role role) {
        // 检查编码是否重复
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getCode, role.getCode());
        if (roleMapper.selectCount(wrapper) > 0) {
            return ResponseResult.error("角色编码已存在");
        }
        role.setStatus(1);
        role.setIsSystem(0);
        roleMapper.insert(role);
        return ResponseResult.success("角色创建成功");
    }

    /**
     * 修改角色
     */
    @PutMapping("/{roleId}")
    public ResponseResult<String> updateRole(
            @PathVariable Long roleId,
            @RequestBody Role role) {
        Role existing = roleMapper.selectById(roleId);
        if (existing == null) {
            return ResponseResult.error("角色不存在");
        }
        if (existing.getIsSystem() == 1) {
            return ResponseResult.error("不能修改系统内置角色");
        }
        role.setId(roleId);
        roleMapper.updateById(role);
        return ResponseResult.success("角色修改成功");
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{roleId}")
    public ResponseResult<String> deleteRole(@PathVariable Long roleId) {
        Role existing = roleMapper.selectById(roleId);
        if (existing == null) {
            return ResponseResult.error("角色不存在");
        }
        if (existing.getIsSystem() == 1) {
            return ResponseResult.error("不能删除系统内置角色");
        }
        // 检查是否有用户关联
        LambdaQueryWrapper<UserRole> urWrapper = new LambdaQueryWrapper<>();
        urWrapper.eq(UserRole::getRoleId, roleId);
        if (userRoleMapper.selectCount(urWrapper) > 0) {
            return ResponseResult.error("该角色下还有用户，不能删除");
        }
        // 删除角色权限关联
        LambdaQueryWrapper<RolePermission> rpWrapper = new LambdaQueryWrapper<>();
        rpWrapper.eq(RolePermission::getRoleId, roleId);
        rolePermissionMapper.delete(rpWrapper);
        // 删除角色
        roleMapper.deleteById(roleId);
        return ResponseResult.success("角色删除成功");
    }

    /**
     * 查询角色详情（含权限列表）
     */
    @GetMapping("/{roleId}")
    public ResponseResult<Map<String, Object>> getRoleDetail(@PathVariable Long roleId) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            return ResponseResult.error("角色不存在");
        }
        // 查询角色的权限
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(wrapper);
        List<Long> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .collect(java.util.stream.Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("role", role);
        result.put("permissionIds", permissionIds);
        return ResponseResult.success(result);
    }

    // ========== 权限管理 ==========

    /**
     * 查询权限列表（树形）
     */
    @GetMapping("/permissions")
    public ResponseResult<List<Permission>> getPermissionList() {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getStatus, 1);
        wrapper.orderByAsc(Permission::getSortOrder);
        return ResponseResult.success(permissionMapper.selectList(wrapper));
    }

    /**
     * 创建权限
     */
    @PostMapping("/permissions")
    public ResponseResult<String> createPermission(@RequestBody Permission permission) {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getCode, permission.getCode());
        if (permissionMapper.selectCount(wrapper) > 0) {
            return ResponseResult.error("权限编码已存在");
        }
        permission.setStatus(1);
        permissionMapper.insert(permission);
        return ResponseResult.success("权限创建成功");
    }

    /**
     * 修改权限
     */
    @PutMapping("/permissions/{permissionId}")
    public ResponseResult<String> updatePermission(
            @PathVariable Long permissionId,
            @RequestBody Permission permission) {
        Permission existing = permissionMapper.selectById(permissionId);
        if (existing == null) {
            return ResponseResult.error("权限不存在");
        }
        permission.setId(permissionId);
        permissionMapper.updateById(permission);
        return ResponseResult.success("权限修改成功");
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/permissions/{permissionId}")
    public ResponseResult<String> deletePermission(@PathVariable Long permissionId) {
        permissionMapper.deleteById(permissionId);
        return ResponseResult.success("权限删除成功");
    }

    // ========== 用户角色关联 ==========

    /**
     * 分配角色给用户
     */
    @PostMapping("/assign")
    public ResponseResult<String> assignRole(
            @RequestParam Long userId,
            @RequestParam Long roleId) {
        // 检查是否已分配
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId).eq(UserRole::getRoleId, roleId);
        if (userRoleMapper.selectCount(wrapper) > 0) {
            return ResponseResult.error("该用户已拥有此角色");
        }
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRoleMapper.insert(userRole);
        return ResponseResult.success("角色分配成功");
    }

    /**
     * 移除用户角色
     */
    @DeleteMapping("/assign")
    public ResponseResult<String> removeRole(
            @RequestParam Long userId,
            @RequestParam Long roleId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId).eq(UserRole::getRoleId, roleId);
        userRoleMapper.delete(wrapper);
        return ResponseResult.success("角色已移除");
    }

    /**
     * 查询用户的角色列表
     */
    @GetMapping("/user/{userId}/roles")
    public ResponseResult<List<Role>> getUserRoles(@PathVariable Long userId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(java.util.stream.Collectors.toList());
        if (roleIds.isEmpty()) {
            return ResponseResult.success(List.of());
        }
        List<Role> roles = roleMapper.selectBatchIds(roleIds);
        return ResponseResult.success(roles);
    }

    // ========== 角色权限关联 ==========

    /**
     * 为角色分配权限
     */
    @PostMapping("/{roleId}/permissions")
    public ResponseResult<String> assignPermissions(
            @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds) {
        // 先删除旧的关联
        LambdaQueryWrapper<RolePermission> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(RolePermission::getRoleId, roleId);
        rolePermissionMapper.delete(deleteWrapper);
        // 添加新的关联
        for (Long permissionId : permissionIds) {
            RolePermission rp = new RolePermission();
            rp.setRoleId(roleId);
            rp.setPermissionId(permissionId);
            rolePermissionMapper.insert(rp);
        }
        return ResponseResult.success("权限分配成功");
    }

    /**
     * 查询角色的权限列表
     */
    @GetMapping("/{roleId}/permissions")
    public ResponseResult<List<Permission>> getRolePermissions(@PathVariable Long roleId) {
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(wrapper);
        List<Long> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .collect(java.util.stream.Collectors.toList());
        if (permissionIds.isEmpty()) {
            return ResponseResult.success(List.of());
        }
        List<Permission> permissions = permissionMapper.selectBatchIds(permissionIds);
        return ResponseResult.success(permissions);
    }
}
