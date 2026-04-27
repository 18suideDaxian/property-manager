package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("room")
public class Room {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 所属小区ID */
    private Long communityId;

    /** 所属楼栋ID */
    private Long buildingId;

    /** 所属单元ID（可为空，独栋无单元） */
    private Long unitId;

    /** 房间号 */
    private String roomNo;

    /** 楼层 */
    private Integer floor;

    /** 建筑面积（平方米） */
    private BigDecimal buildingArea;

    /** 套内面积（平方米） */
    private BigDecimal innerArea;

    /** 房屋类型：1-住宅 2-商铺 3-车库 4-其他 */
    private Integer roomType;

    /** 状态：1-已售 2-未售 3-装修中 */
    private Integer status;

    /** 备注 */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
