package com.property.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.AdminUser;
import com.property.management.entity.OwnerUser;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<AdminUser> {

    /**
     * 业主查询个人信息
     */
    ResponseResult<OwnerUser> getOwnerProfile(Long ownerId);

    /**
     * 业主修改个人信息
     */
    ResponseResult<String> updateOwnerProfile(Long ownerId, String realName, String nickname, String avatar);

    /**
     * 业主修改密码
     */
    ResponseResult<String> changeOwnerPassword(Long ownerId, String oldPassword, String newPassword);

    /**
     * 管理员查询业主列表
     */
    ResponseResult<List<OwnerUser>> getOwnerList(String phone, String realName, Integer status, Integer page, Integer size);

    /**
     * 管理员查询业主详情
     */
    ResponseResult<OwnerUser> getOwnerDetail(Long ownerId);

    /**
     * 管理员启用/禁用业主账号
     */
    ResponseResult<String> updateOwnerStatus(Long ownerId, Integer status);

    /**
     * 管理员查询管理员列表
     */
    ResponseResult<List<AdminUser>> getAdminList(String username, String realName, Integer status, Integer page, Integer size);

    /**
     * 管理员添加管理员
     */
    ResponseResult<String> createAdmin(AdminUser adminUser);

    /**
     * 管理员修改管理员信息
     */
    ResponseResult<String> updateAdmin(AdminUser adminUser);

    /**
     * 管理员启用/禁用管理员账号
     */
    ResponseResult<String> updateAdminStatus(Long adminId, Integer status);

    /**
     * 管理员重置密码
     */
    ResponseResult<String> resetPassword(Long userId, String userType);

    /**
     * 查询用户统计
     */
    ResponseResult<Map<String, Object>> getUserStatistics();
}
