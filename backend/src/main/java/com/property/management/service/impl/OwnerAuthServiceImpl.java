package com.property.management.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.dto.LoginDTO;
import com.property.management.dto.RegisterDTO;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.OwnerUser;
import com.property.management.mapper.OwnerUserMapper;
import com.property.management.service.OwnerAuthService;
import com.property.management.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OwnerAuthServiceImpl extends ServiceImpl<OwnerUserMapper, OwnerUser> implements OwnerAuthService {
    
    @Autowired
    private OwnerUserMapper ownerUserMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public ResponseResult<String> register(RegisterDTO registerDTO) {
        // 校验手机号是否已存在
        LambdaQueryWrapper<OwnerUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OwnerUser::getPhone, registerDTO.getPhone());
        if (ownerUserMapper.selectCount(queryWrapper) > 0) {
            return ResponseResult.error("手机号已被注册");
        }
        
        // 校验密码一致性
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            return ResponseResult.error("两次输入的密码不一致");
        }
        
        // 创建用户
        OwnerUser ownerUser = new OwnerUser();
        ownerUser.setPhone(registerDTO.getPhone());
        ownerUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        ownerUser.setRealName(registerDTO.getRealName());
        ownerUser.setStatus(1);
        
        int result = ownerUserMapper.insert(ownerUser);
        
        if (result > 0) {
            return ResponseResult.success("注册成功");
        } else {
            return ResponseResult.error("注册失败");
        }
    }
    
    @Override
    public ResponseResult<String> login(LoginDTO loginDTO) {
        // 查询用户
        LambdaQueryWrapper<OwnerUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OwnerUser::getPhone, loginDTO.getPhone());
        OwnerUser ownerUser = ownerUserMapper.selectOne(queryWrapper);
        
        if (ownerUser == null) {
            return ResponseResult.error("用户不存在");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), ownerUser.getPassword())) {
            return ResponseResult.error("密码错误");
        }
        
        // 生成JWT Token
        String token = jwtUtil.generateToken(
                ownerUser.getId(), 
                ownerUser.getPhone(), 
                "OWNER"
        );
        
        return ResponseResult.success(token);
    }
}