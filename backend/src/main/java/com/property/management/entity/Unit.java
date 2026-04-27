package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("unit")
public class Unit {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 所属楼栋ID */
    private Long buildingId;

    /** 单元名称/编号 */
    private String name;

    /** 每层户数 */
    private Integer roomsPerFloor;

    /** 状态：1-启用 0-禁用 */
    private Integer status;

    /** 备注 */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
