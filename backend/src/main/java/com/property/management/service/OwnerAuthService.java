package com.property.management.service;

import com.property.management.dto.LoginDTO;
import com.property.management.dto.RegisterDTO;
import com.property.management.dto.ResponseResult;

public interface OwnerAuthService {
    
    /**
     * 业主注册
     */
    ResponseResult<String> register(RegisterDTO registerDTO);
    
    /**
     * 业主登录
     */
    ResponseResult<String> login(LoginDTO loginDTO);
}