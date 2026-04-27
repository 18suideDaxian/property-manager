package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("operation_log")
public class OperationLog {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 操作人ID */
    private Long operatorId;

    /** 操作人姓名 */
    private String operatorName;

    /** 操作人类型：ADMIN-管理员 OWNER-业主 */
    private String operatorType;

    /** 操作模块 */
    private String module;

    /** 操作类型 */
    private String operation;

    /** 请求方法（GET/POST/PUT/DELETE） */
    private String requestMethod;

    /** 请求URL */
    private String requestUrl;

    /** 请求参数 */
    private String requestParams;

    /** 响应结果 */
    private String responseResult;

    /** 操作IP */
    private String ip;

    /** 耗时（毫秒） */
    private Long duration;

    /** 状态：1-成功 0-失败 */
    private Integer status;

    /** 错误信息 */
    private String errorMsg;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
