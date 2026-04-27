package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("repair_order")
public class RepairOrder {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 工单编号 */
    private String orderNo;

    /** 报修类型ID */
    private Long repairTypeId;

    /** 报修类型名称（冗余） */
    private String repairTypeName;

    /** 报修业主ID */
    private Long ownerId;

    /** 报修房产ID */
    private Long roomId;

    /** 所属小区ID */
    private Long communityId;

    /** 报修地址描述 */
    private String address;

    /** 问题描述 */
    private String description;

    /** 图片URL（多个用逗号分隔） */
    private String images;

    /** 联系人 */
    private String contactName;

    /** 联系电话 */
    private String contactPhone;

    /** 期望上门时间 */
    private LocalDateTime expectedTime;

    /** 紧急程度：1-普通 2-紧急 3-非常紧急 */
    private Integer urgencyLevel;

    /** 状态：1-待受理 2-已受理 3-维修中 4-已完成 5-已评价 6-已关闭 */
    private Integer status;

    /** 受理人ID */
    private Long handlerId;

    /** 受理时间 */
    private LocalDateTime handleTime;

    /** 完成时间 */
    private LocalDateTime completeTime;

    /** 维修备注（维修人员填写） */
    private String handleRemark;

    /** 评分：1-5 */
    private Integer rating;

    /** 评价内容 */
    private String ratingContent;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
