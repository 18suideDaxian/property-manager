package com.property.management.controller;

import com.property.management.dto.ResponseResult;
import com.property.management.entity.OwnerUser;
import com.property.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理员-用户管理控制器
 */
@RestController
@RequestMapping("/admin/user")
@CrossOrigin
public class AdminUserController {

    @Autowired
    private UserService userService;

    /**
     * 查询业主列表
     */
    @GetMapping("/owners")
    public ResponseResult<List<OwnerUser>> getOwnerList(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return userService.getOwnerList(phone, realName, status, page, size);
    }

    /**
     * 查询业主详情
     */
    @GetMapping("/owners/{ownerId}")
    public ResponseResult<OwnerUser> getOwnerDetail(@PathVariable Long ownerId) {
        return userService.getOwnerDetail(ownerId);
    }

    /**
     * 启用/禁用业主账号
     */
    @PutMapping("/owners/{ownerId}/status")
    public ResponseResult<String> updateOwnerStatus(
            @PathVariable Long ownerId,
            @RequestParam Integer status) {
        return userService.updateOwnerStatus(ownerId, status);
    }

    /**
     * 查询管理员列表
     */
    @GetMapping("/admins")
    public ResponseResult<?> getAdminList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return userService.getAdminList(username, realName, status, page, size);
    }

    /**
     * 添加管理员
     */
    @PostMapping("/admins")
    public ResponseResult<String> createAdmin(@RequestBody com.property.management.entity.AdminUser adminUser) {
        return userService.createAdmin(adminUser);
    }

    /**
     * 修改管理员信息
     */
    @PutMapping("/admins/{adminId}")
    public ResponseResult<String> updateAdmin(
            @PathVariable Long adminId,
            @RequestBody com.property.management.entity.AdminUser adminUser) {
        adminUser.setId(adminId);
        return userService.updateAdmin(adminUser);
    }

    /**
     * 启用/禁用管理员账号
     */
    @PutMapping("/admins/{adminId}/status")
    public ResponseResult<String> updateAdminStatus(
            @PathVariable Long adminId,
            @RequestParam Integer status) {
        return userService.updateAdminStatus(adminId, status);
    }

    /**
     * 重置密码
     */
    @PostMapping("/reset-password")
    public ResponseResult<String> resetPassword(
            @RequestParam Long userId,
            @RequestParam String userType) {
        return userService.resetPassword(userId, userType);
    }

    /**
     * 查询用户统计
     */
    @GetMapping("/statistics")
    public ResponseResult<Map<String, Object>> getUserStatistics() {
        return userService.getUserStatistics();
    }
}
