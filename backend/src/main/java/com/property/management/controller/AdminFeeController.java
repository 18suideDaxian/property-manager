package com.property.management.controller;

import com.property.management.dto.ResponseResult;
import com.property.management.entity.FeeBill;
import com.property.management.entity.PaymentRecord;
import com.property.management.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理员-缴费管理控制器
 */
@RestController
@RequestMapping("/admin/fee")
@CrossOrigin
public class AdminFeeController {

    @Autowired
    private FeeService feeService;

    /**
     * 查询所有账单
     */
    @GetMapping("/bills")
    public ResponseResult<List<FeeBill>> getAllBills(
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String billNo,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return feeService.getAllBills(communityId, ownerId, status, billNo, page, size);
    }

    /**
     * 生成账单
     */
    @PostMapping("/bills")
    public ResponseResult<String> generateBill(@RequestBody FeeBill feeBill,
                                               @RequestAttribute("userId") Long operatorId) {
        feeBill.setCreatedBy(operatorId);
        return feeService.generateBill(feeBill);
    }

    /**
     * 批量生成账单
     */
    @PostMapping("/bills/batch")
    public ResponseResult<String> batchGenerateBills(
            @RequestParam Long communityId,
            @RequestParam Long feeTypeId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return feeService.batchGenerateBills(communityId, feeTypeId, startDate, endDate);
    }

    /**
     * 作废账单
     */
    @PostMapping("/bills/{billId}/cancel")
    public ResponseResult<String> cancelBill(@PathVariable Long billId) {
        return feeService.cancelBill(billId);
    }

    /**
     * 管理员代缴
     */
    @PostMapping("/bills/{billId}/admin-pay")
    public ResponseResult<PaymentRecord> adminPayBill(
            @PathVariable Long billId,
            @RequestParam(defaultValue = "1") Integer payMethod,
            @RequestAttribute("userId") Long operatorId) {
        return feeService.adminPayBill(billId, payMethod, operatorId);
    }

    /**
     * 查询缴费统计
     */
    @GetMapping("/statistics")
    public ResponseResult<Map<String, Object>> getPaymentStatistics(
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return feeService.getPaymentStatistics(communityId, startDate, endDate);
    }
}
