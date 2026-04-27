package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("building")
public class Building {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 所属小区ID */
    private Long communityId;

    /** 楼栋名称/编号 */
    private String name;

    /** 楼层数 */
    private Integer floorCount;

    /** 楼栋类型：1-住宅 2-商业 3-综合 */
    private Integer buildingType;

    /** 状态：1-启用 0-禁用 */
    private Integer status;

    /** 备注 */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
