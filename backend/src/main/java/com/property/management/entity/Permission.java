package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("permission")
public class Permission {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 权限名称 */
    private String name;

    /** 权限编码 */
    private String code;

    /** 权限类型：1-菜单 2-按钮 3-接口 */
    private Integer type;

    /** 父级权限ID（0表示顶级） */
    private Long parentId;

    /** 路由路径（菜单类型使用） */
    private String path;

    /** 组件路径（菜单类型使用） */
    private String component;

    /** 图标（菜单类型使用） */
    private String icon;

    /** 排序号 */
    private Integer sortOrder;

    /** 状态：1-启用 0-禁用 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
