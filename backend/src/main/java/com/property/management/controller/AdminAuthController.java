package com.property.management.controller;

import com.property.management.dto.LoginDTO;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.AdminUser;
import com.property.management.service.AdminAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/auth")
@CrossOrigin
public class AdminAuthController {
    
    @Autowired
    private AdminAuthService adminAuthService;
    
    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public ResponseResult<String> login(@RequestBody LoginDTO loginDTO) {
        return adminAuthService.login(loginDTO);
    }
}