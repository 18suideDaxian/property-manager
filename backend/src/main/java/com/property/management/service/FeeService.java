package com.property.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.FeeBill;
import com.property.management.entity.PaymentRecord;

import java.util.List;
import java.util.Map;

public interface FeeService extends IService<FeeBill> {

    /**
     * 业主查询自己的账单列表
     */
    ResponseResult<List<FeeBill>> getOwnerBills(Long ownerId, Integer status, Integer page, Integer size);

    /**
     * 业主查询账单详情
     */
    ResponseResult<FeeBill> getBillDetail(Long billId, Long ownerId);

    /**
     * 业主缴费（模拟支付）
     */
    ResponseResult<PaymentRecord> payBill(Long billId, Long ownerId, Integer payMethod);

    /**
     * 业主查询缴费记录
     */
    ResponseResult<List<PaymentRecord>> getPaymentRecords(Long ownerId, Integer page, Integer size);

    /**
     * 管理员查询所有账单（支持筛选）
     */
    ResponseResult<List<FeeBill>> getAllBills(Long communityId, Long ownerId, Integer status, String billNo, Integer page, Integer size);

    /**
     * 管理员生成账单
     */
    ResponseResult<String> generateBill(FeeBill feeBill);

    /**
     * 管理员批量生成账单
     */
    ResponseResult<String> batchGenerateBills(Long communityId, Long feeTypeId, String startDate, String endDate);

    /**
     * 管理员作废账单
     */
    ResponseResult<String> cancelBill(Long billId);

    /**
     * 管理员代缴
     */
    ResponseResult<PaymentRecord> adminPayBill(Long billId, Integer payMethod, Long operatorId);

    /**
     * 查询缴费统计
     */
    ResponseResult<Map<String, Object>> getPaymentStatistics(Long communityId, String startDate, String endDate);
}
