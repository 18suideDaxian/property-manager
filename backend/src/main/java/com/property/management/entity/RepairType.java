package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("repair_type")
public class RepairType {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 报修类型名称（如：水电维修、门窗维修、电梯故障） */
    private String name;

    /** 类型编码 */
    private String code;

    /** 所属分类：1-公共区域 2-室内 3-设备设施 */
    private Integer category;

    /** 预计处理时长（小时） */
    private Integer expectedHours;

    /** 状态：1-启用 0-禁用 */
    private Integer status;

    /** 排序号 */
    private Integer sortOrder;

    /** 备注 */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
