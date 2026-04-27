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
 * 业主缴费控制器
 */
@RestController
@RequestMapping("/owner/fee")
@CrossOrigin
public class OwnerFeeController {

    @Autowired
    private FeeService feeService;

    /**
     * 查询我的账单列表
     */
    @GetMapping("/bills")
    public ResponseResult<List<FeeBill>> getMyBills(
            @RequestAttribute("userId") Long ownerId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return feeService.getOwnerBills(ownerId, status, page, size);
    }

    /**
     * 查询账单详情
     */
    @GetMapping("/bills/{billId}")
    public ResponseResult<FeeBill> getBillDetail(
            @RequestAttribute("userId") Long ownerId,
            @PathVariable Long billId) {
        return feeService.getBillDetail(billId, ownerId);
    }

    /**
     * 缴费
     */
    @PostMapping("/bills/{billId}/pay")
    public ResponseResult<PaymentRecord> payBill(
            @RequestAttribute("userId") Long ownerId,
            @PathVariable Long billId,
            @RequestParam(defaultValue = "1") Integer payMethod) {
        return feeService.payBill(billId, ownerId, payMethod);
    }

    /**
     * 查询我的缴费记录
     */
    @GetMapping("/payments")
    public ResponseResult<List<PaymentRecord>> getPaymentRecords(
            @RequestAttribute("userId") Long ownerId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return feeService.getPaymentRecords(ownerId, page, size);
    }
}
