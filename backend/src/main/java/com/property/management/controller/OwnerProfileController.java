package com.property.management.controller;

import com.property.management.dto.ResponseResult;
import com.property.management.entity.OwnerUser;
import com.property.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 业主个人信息控制器
 */
@RestController
@RequestMapping("/owner/profile")
@CrossOrigin
public class OwnerProfileController {

    @Autowired
    private UserService userService;

    /**
     * 查询个人信息
     */
    @GetMapping("")
    public ResponseResult<OwnerUser> getProfile(
            @RequestAttribute("userId") Long ownerId) {
        return userService.getOwnerProfile(ownerId);
    }

    /**
     * 修改个人信息
     */
    @PutMapping("")
    public ResponseResult<String> updateProfile(
            @RequestAttribute("userId") Long ownerId,
            @RequestBody Map<String, String> params) {
        return userService.updateOwnerProfile(
                ownerId,
                params.get("realName"),
                params.get("nickname"),
                params.get("avatar")
        );
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public ResponseResult<String> changePassword(
            @RequestAttribute("userId") Long ownerId,
            @RequestBody Map<String, String> params) {
        return userService.changeOwnerPassword(
                ownerId,
                params.get("oldPassword"),
                params.get("newPassword")
        );
    }
}
