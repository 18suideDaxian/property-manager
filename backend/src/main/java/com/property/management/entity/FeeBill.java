package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fee_bill")
public class FeeBill {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 账单编号 */
    private String billNo;

    /** 房产ID */
    private Long roomId;

    /** 业主ID */
    private Long ownerId;

    /** 费用类型ID */
    private Long feeTypeId;

    /** 费用类型名称（冗余，方便查询） */
    private String feeTypeName;

    /** 所属小区ID */
    private Long communityId;

    /** 账单金额 */
    private BigDecimal amount;

    /** 滞纳金 */
    private BigDecimal lateFee;

    /** 已缴金额 */
    private BigDecimal paidAmount;

    /** 账单周期开始日期 */
    private LocalDate billStartDate;

    /** 账单周期结束日期 */
    private LocalDate billEndDate;

    /** 应缴日期 */
    private LocalDate dueDate;

    /** 状态：1-待缴费 2-部分缴费 3-已缴费 4-已逾期 5-已作废 */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 生成人ID */
    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
