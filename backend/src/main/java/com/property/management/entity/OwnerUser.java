package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("owner_user")
public class OwnerUser {
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String phone;
    
    private String password;
    
    private String realName;
    
    private String nickname;
    
    private String avatar;
    
    private Integer status;
    
    private LocalDateTime lastLoginTime;
    
    private String lastLoginIp;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}