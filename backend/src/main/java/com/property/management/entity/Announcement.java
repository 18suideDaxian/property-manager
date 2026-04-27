package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("announcement")
public class Announcement {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 所属小区ID */
    private Long communityId;

    /** 公告标题 */
    private String title;

    /** 公告内容 */
    private String content;

    /** 公告类型：1-通知 2-公告 3-温馨提示 4-紧急通知 */
    private Integer type;

    /** 是否置顶：1-是 0-否 */
    private Integer isTop;

    /** 阅读次数 */
    private Integer readCount;

    /** 状态：1-草稿 2-已发布 3-已撤回 */
    private Integer status;

    /** 发布人ID */
    private Long publisherId;

    /** 发布人姓名 */
    private String publisherName;

    /** 发布时间 */
    private LocalDateTime publishTime;

    /** 撤回时间 */
    private LocalDateTime revokeTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
