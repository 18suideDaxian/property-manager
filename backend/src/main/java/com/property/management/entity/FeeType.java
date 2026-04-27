package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fee_type")
public class FeeType {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 费用名称（如：物业费、水费、电费） */
    private String name;

    /** 费用编码 */
    private String code;

    /** 计费单位：1-按面积 2-按户 3-按用量 4-固定金额 */
    private Integer billingUnit;

    /** 单价（按面积/按用量时使用） */
    private BigDecimal unitPrice;

    /** 固定金额（按户/固定金额时使用） */
    private BigDecimal fixedAmount;

    /** 缴费周期：1-月 2-季 3-半年 4-年 5-一次性 */
    private Integer billingCycle;

    /** 滞纳金比例（每天，百分比） */
    private BigDecimal lateFeeRate;

    /** 状态：1-启用 0-禁用 */
    private Integer status;

    /** 备注 */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
