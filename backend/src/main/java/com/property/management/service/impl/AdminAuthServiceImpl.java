package com.property.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.dto.LoginDTO;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.AdminUser;
import com.property.management.mapper.AdminUserMapper;
import com.property.management.service.AdminAuthService;
import com.property.management.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminAuthService {
    
    @Autowired
    private AdminUserMapper adminUserMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public ResponseResult<String> login(LoginDTO loginDTO) {
        // 查询管理员
        LambdaQueryWrapper<AdminUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminUser::getUsername, loginDTO.getPhone());
        AdminUser adminUser = adminUserMapper.selectOne(queryWrapper);
        
        if (adminUser == null) {
            return ResponseResult.error("管理员不存在");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), adminUser.getPassword())) {
            return ResponseResult.error("密码错误");
        }
        
        // 生成JWT Token
        String token = jwtUtil.generateToken(
                adminUser.getId(), 
                adminUser.getUsername(), 
                "ADMIN"
        );
        
        return ResponseResult.success(token);
    }
}