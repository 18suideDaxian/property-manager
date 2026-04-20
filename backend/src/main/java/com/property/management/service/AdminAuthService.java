package com.property.management.service;

import com.property.management.dto.LoginDTO;
import com.property.management.dto.ResponseResult;

public interface AdminAuthService {
    
    /**
     * 管理员登录
     */
    ResponseResult<String> login(LoginDTO loginDTO);
}