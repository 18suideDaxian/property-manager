package com.property.management.controller;

import com.property.management.dto.LoginDTO;
import com.property.management.dto.RegisterDTO;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.OwnerUser;
import com.property.management.service.OwnerAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner/auth")
@CrossOrigin
public class OwnerAuthController {
    
    @Autowired
    private OwnerAuthService ownerAuthService;
    
    /**
     * 业主注册
     */
    @PostMapping("/register")
    public ResponseResult<String> register(@RequestBody RegisterDTO registerDTO) {
        return ownerAuthService.register(registerDTO);
    }
    
    /**
     * 业主登录
     */
    @PostMapping("/login")
    public ResponseResult<String> login(@RequestBody LoginDTO loginDTO) {
        return ownerAuthService.login(loginDTO);
    }
}