package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("owner_room")
public class OwnerRoom {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 业主用户ID */
    private Long ownerId;

    /** 房产ID */
    private Long roomId;

    /** 关系类型：1-业主 2-租户 3-家属 */
    private Integer relationType;

    /** 是否为主要产权人：1-是 0-否 */
    private Integer isPrimary;

    /** 入住时间 */
    private LocalDateTime checkInTime;

    /** 状态：1-有效 0-无效 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
