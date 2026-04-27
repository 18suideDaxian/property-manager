package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("role")
public class Role {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 角色名称 */
    private String name;

    /** 角色编码 */
    private String code;

    /** 角色描述 */
    private String description;

    /** 状态：1-启用 0-禁用 */
    private Integer status;

    /** 排序号 */
    private Integer sortOrder;

    /** 是否系统内置角色：1-是 0-否 */
    private Integer isSystem;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
