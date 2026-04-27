package com.property.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.AdminUser;
import com.property.management.entity.OwnerUser;
import com.property.management.mapper.AdminUserMapper;
import com.property.management.mapper.OwnerUserMapper;
import com.property.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements UserService {

    @Autowired
    private OwnerUserMapper ownerUserMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult<OwnerUser> getOwnerProfile(Long ownerId) {
        OwnerUser owner = ownerUserMapper.selectById(ownerId);
        if (owner == null) {
            return ResponseResult.error("用户不存在");
        }
        owner.setPassword(null); // 不返回密码
        return ResponseResult.success(owner);
    }

    @Override
    public ResponseResult<String> updateOwnerProfile(Long ownerId, String realName, String nickname, String avatar) {
        OwnerUser owner = ownerUserMapper.selectById(ownerId);
        if (owner == null) {
            return ResponseResult.error("用户不存在");
        }
        if (realName != null) owner.setRealName(realName);
        if (nickname != null) owner.setNickname(nickname);
        if (avatar != null) owner.setAvatar(avatar);
        ownerUserMapper.updateById(owner);
        return ResponseResult.success("个人信息修改成功");
    }

    @Override
    public ResponseResult<String> changeOwnerPassword(Long ownerId, String oldPassword, String newPassword) {
        OwnerUser owner = ownerUserMapper.selectById(ownerId);
        if (owner == null) {
            return ResponseResult.error("用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, owner.getPassword())) {
            return ResponseResult.error("原密码错误");
        }
        owner.setPassword(passwordEncoder.encode(newPassword));
        ownerUserMapper.updateById(owner);
        return ResponseResult.success("密码修改成功");
    }

    @Override
    public ResponseResult<List<OwnerUser>> getOwnerList(String phone, String realName, Integer status, Integer page, Integer size) {
        Page<OwnerUser> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<OwnerUser> wrapper = new LambdaQueryWrapper<>();
        if (phone != null && !phone.isEmpty()) {
            wrapper.like(OwnerUser::getPhone, phone);
        }
        if (realName != null && !realName.isEmpty()) {
            wrapper.like(OwnerUser::getRealName, realName);
        }
        if (status != null) {
            wrapper.eq(OwnerUser::getStatus, status);
        }
        wrapper.orderByDesc(OwnerUser::getCreateTime);
        ownerUserMapper.selectPage(pageParam, wrapper);
        // 清除密码
        pageParam.getRecords().forEach(u -> u.setPassword(null));
        return ResponseResult.success(pageParam.getRecords());
    }

    @Override
    public ResponseResult<OwnerUser> getOwnerDetail(Long ownerId) {
        OwnerUser owner = ownerUserMapper.selectById(ownerId);
        if (owner == null) {
            return ResponseResult.error("用户不存在");
        }
        owner.setPassword(null);
        return ResponseResult.success(owner);
    }

    @Override
    public ResponseResult<String> updateOwnerStatus(Long ownerId, Integer status) {
        OwnerUser owner = ownerUserMapper.selectById(ownerId);
        if (owner == null) {
            return ResponseResult.error("用户不存在");
        }
        owner.setStatus(status);
        ownerUserMapper.updateById(owner);
        return ResponseResult.success(status == 1 ? "账号已启用" : "账号已禁用");
    }

    @Override
    public ResponseResult<List<AdminUser>> getAdminList(String username, String realName, Integer status, Integer page, Integer size) {
        Page<AdminUser> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.like(AdminUser::getUsername, username);
        }
        if (realName != null && !realName.isEmpty()) {
            wrapper.like(AdminUser::getRealName, realName);
        }
        if (status != null) {
            wrapper.eq(AdminUser::getStatus, status);
        }
        wrapper.orderByDesc(AdminUser::getCreateTime);
        adminUserMapper.selectPage(pageParam, wrapper);
        pageParam.getRecords().forEach(u -> u.setPassword(null));
        return ResponseResult.success(pageParam.getRecords());
    }

    @Override
    public ResponseResult<String> createAdmin(AdminUser adminUser) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUser::getUsername, adminUser.getUsername());
        if (adminUserMapper.selectCount(wrapper) > 0) {
            return ResponseResult.error("用户名已存在");
        }
        adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
        adminUser.setStatus(1);
        adminUser.setIsSuperAdmin(0);
        adminUserMapper.insert(adminUser);
        return ResponseResult.success("管理员创建成功");
    }

    @Override
    public ResponseResult<String> updateAdmin(AdminUser adminUser) {
        AdminUser existing = adminUserMapper.selectById(adminUser.getId());
        if (existing == null) {
            return ResponseResult.error("管理员不存在");
        }
        // 不允许修改超级管理员
        if (existing.getIsSuperAdmin() == 1) {
            return ResponseResult.error("不能修改超级管理员信息");
        }
        // 只更新允许的字段
        if (adminUser.getRealName() != null) existing.setRealName(adminUser.getRealName());
        if (adminUser.getPhone() != null) existing.setPhone(adminUser.getPhone());
        if (adminUser.getEmail() != null) existing.setEmail(adminUser.getEmail());
        if (adminUser.getAvatar() != null) existing.setAvatar(adminUser.getAvatar());
        adminUserMapper.updateById(existing);
        return ResponseResult.success("管理员信息修改成功");
    }

    @Override
    public ResponseResult<String> updateAdminStatus(Long adminId, Integer status) {
        AdminUser admin = adminUserMapper.selectById(adminId);
        if (admin == null) {
            return ResponseResult.error("管理员不存在");
        }
        if (admin.getIsSuperAdmin() == 1) {
            return ResponseResult.error("不能禁用超级管理员");
        }
        admin.setStatus(status);
        adminUserMapper.updateById(admin);
        return ResponseResult.success(status == 1 ? "账号已启用" : "账号已禁用");
    }

    @Override
    public ResponseResult<String> resetPassword(Long userId, String userType) {
        String defaultPassword = passwordEncoder.encode("123456");
        if ("OWNER".equals(userType)) {
            OwnerUser owner = ownerUserMapper.selectById(userId);
            if (owner == null) {
                return ResponseResult.error("用户不存在");
            }
            owner.setPassword(defaultPassword);
            ownerUserMapper.updateById(owner);
        } else if ("ADMIN".equals(userType)) {
            AdminUser admin = adminUserMapper.selectById(userId);
            if (admin == null) {
                return ResponseResult.error("用户不存在");
            }
            if (admin.getIsSuperAdmin() == 1) {
                return ResponseResult.error("不能重置超级管理员密码");
            }
            admin.setPassword(defaultPassword);
            adminUserMapper.updateById(admin);
        } else {
            return ResponseResult.error("无效的用户类型");
        }
        return ResponseResult.success("密码已重置为 123456");
    }

    @Override
    public ResponseResult<Map<String, Object>> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // 业主统计
        long totalOwners = ownerUserMapper.selectCount(null);
        LambdaQueryWrapper<OwnerUser> activeOwnerWrapper = new LambdaQueryWrapper<>();
        activeOwnerWrapper.eq(OwnerUser::getStatus, 1);
        long activeOwners = ownerUserMapper.selectCount(activeOwnerWrapper);

        // 管理员统计
        long totalAdmins = adminUserMapper.selectCount(null);
        LambdaQueryWrapper<AdminUser> activeAdminWrapper = new LambdaQueryWrapper<>();
        activeAdminWrapper.eq(AdminUser::getStatus, 1);
        long activeAdmins = adminUserMapper.selectCount(activeAdminWrapper);

        stats.put("totalOwners", totalOwners);
        stats.put("activeOwners", activeOwners);
        stats.put("disabledOwners", totalOwners - activeOwners);
        stats.put("totalAdmins", totalAdmins);
        stats.put("activeAdmins", activeAdmins);
        stats.put("disabledAdmins", totalAdmins - activeAdmins);

        return ResponseResult.success(stats);
    }
}
