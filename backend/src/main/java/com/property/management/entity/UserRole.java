package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_role")
public class UserRole {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 管理员用户ID */
    private Long userId;

    /** 角色ID */
    private Long roleId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
