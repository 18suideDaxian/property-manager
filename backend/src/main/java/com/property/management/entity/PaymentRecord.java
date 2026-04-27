package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("payment_record")
public class PaymentRecord {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 缴费流水号 */
    private String paymentNo;

    /** 账单ID */
    private Long billId;

    /** 业主ID */
    private Long ownerId;

    /** 缴费金额 */
    private BigDecimal amount;

    /** 支付方式：1-现金 2-微信 3-支付宝 4-银行转账 5-POS机 */
    private Integer payMethod;

    /** 支付平台交易号 */
    private String transactionNo;

    /** 状态：1-成功 2-退款中 3-已退款 */
    private Integer status;

    /** 缴费时间 */
    private LocalDateTime payTime;

    /** 操作人ID（管理员代缴时有值） */
    private Long operatorId;

    /** 备注 */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
