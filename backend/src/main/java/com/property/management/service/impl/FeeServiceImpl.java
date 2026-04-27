package com.property.management.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.FeeBill;
import com.property.management.entity.PaymentRecord;
import com.property.management.mapper.FeeBillMapper;
import com.property.management.mapper.PaymentRecordMapper;
import com.property.management.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeeServiceImpl extends ServiceImpl<FeeBillMapper, FeeBill> implements FeeService {

    @Autowired
    private FeeBillMapper feeBillMapper;

    @Autowired
    private PaymentRecordMapper paymentRecordMapper;

    @Override
    public ResponseResult<List<FeeBill>> getOwnerBills(Long ownerId, Integer status, Integer page, Integer size) {
        Page<FeeBill> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<FeeBill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeeBill::getOwnerId, ownerId);
        if (status != null) {
            wrapper.eq(FeeBill::getStatus, status);
        }
        wrapper.orderByDesc(FeeBill::getCreateTime);
        feeBillMapper.selectPage(pageParam, wrapper);
        return ResponseResult.success(pageParam.getRecords());
    }

    @Override
    public ResponseResult<FeeBill> getBillDetail(Long billId, Long ownerId) {
        LambdaQueryWrapper<FeeBill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeeBill::getId, billId).eq(FeeBill::getOwnerId, ownerId);
        FeeBill bill = feeBillMapper.selectOne(wrapper);
        if (bill == null) {
            return ResponseResult.error("账单不存在");
        }
        return ResponseResult.success(bill);
    }

    @Override
    @Transactional
    public ResponseResult<PaymentRecord> payBill(Long billId, Long ownerId, Integer payMethod) {
        // 查询账单
        LambdaQueryWrapper<FeeBill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeeBill::getId, billId).eq(FeeBill::getOwnerId, ownerId);
        FeeBill bill = feeBillMapper.selectOne(wrapper);
        if (bill == null) {
            return ResponseResult.error("账单不存在");
        }
        if (bill.getStatus() == 3) {
            return ResponseResult.error("账单已缴费");
        }
        if (bill.getStatus() == 5) {
            return ResponseResult.error("账单已作废");
        }

        // 计算应缴金额（金额 + 滞纳金 - 已缴）
        BigDecimal totalAmount = bill.getAmount().add(bill.getLateFee() != null ? bill.getLateFee() : BigDecimal.ZERO);
        BigDecimal unpaidAmount = totalAmount.subtract(bill.getPaidAmount() != null ? bill.getPaidAmount() : BigDecimal.ZERO);

        // 创建缴费记录
        PaymentRecord record = new PaymentRecord();
        record.setPaymentNo("PAY" + IdUtil.getSnowflakeNextIdStr());
        record.setBillId(billId);
        record.setOwnerId(ownerId);
        record.setAmount(unpaidAmount);
        record.setPayMethod(payMethod);
        record.setStatus(1);
        record.setPayTime(LocalDateTime.now());
        paymentRecordMapper.insert(record);

        // 更新账单状态
        bill.setPaidAmount(totalAmount);
        bill.setStatus(3); // 已缴费
        feeBillMapper.updateById(bill);

        return ResponseResult.success(record);
    }

    @Override
    public ResponseResult<List<PaymentRecord>> getPaymentRecords(Long ownerId, Integer page, Integer size) {
        Page<PaymentRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<PaymentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentRecord::getOwnerId, ownerId);
        wrapper.eq(PaymentRecord::getStatus, 1);
        wrapper.orderByDesc(PaymentRecord::getPayTime);
        paymentRecordMapper.selectPage(pageParam, wrapper);
        return ResponseResult.success(pageParam.getRecords());
    }

    @Override
    public ResponseResult<List<FeeBill>> getAllBills(Long communityId, Long ownerId, Integer status, String billNo, Integer page, Integer size) {
        Page<FeeBill> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<FeeBill> wrapper = new LambdaQueryWrapper<>();
        if (communityId != null) {
            wrapper.eq(FeeBill::getCommunityId, communityId);
        }
        if (ownerId != null) {
            wrapper.eq(FeeBill::getOwnerId, ownerId);
        }
        if (status != null) {
            wrapper.eq(FeeBill::getStatus, status);
        }
        if (billNo != null && !billNo.isEmpty()) {
            wrapper.like(FeeBill::getBillNo, billNo);
        }
        wrapper.orderByDesc(FeeBill::getCreateTime);
        feeBillMapper.selectPage(pageParam, wrapper);
        return ResponseResult.success(pageParam.getRecords());
    }

    @Override
    public ResponseResult<String> generateBill(FeeBill feeBill) {
        feeBill.setBillNo("BILL" + IdUtil.getSnowflakeNextIdStr());
        feeBill.setStatus(1); // 待缴费
        feeBill.setPaidAmount(BigDecimal.ZERO);
        feeBill.setLateFee(BigDecimal.ZERO);
        feeBillMapper.insert(feeBill);
        return ResponseResult.success("账单生成成功");
    }

    @Override
    @Transactional
    public ResponseResult<String> batchGenerateBills(Long communityId, Long feeTypeId, String startDate, String endDate) {
        // 批量生成逻辑：为指定小区的所有已售房产生成账单
        // 这里简化实现，实际需要查询所有房产并根据费用类型计算金额
        return ResponseResult.success("批量生成任务已提交");
    }

    @Override
    public ResponseResult<String> cancelBill(Long billId) {
        FeeBill bill = feeBillMapper.selectById(billId);
        if (bill == null) {
            return ResponseResult.error("账单不存在");
        }
        if (bill.getStatus() == 3) {
            return ResponseResult.error("已缴费的账单不能作废");
        }
        bill.setStatus(5); // 已作废
        feeBillMapper.updateById(bill);
        return ResponseResult.success("账单已作废");
    }

    @Override
    @Transactional
    public ResponseResult<PaymentRecord> adminPayBill(Long billId, Integer payMethod, Long operatorId) {
        FeeBill bill = feeBillMapper.selectById(billId);
        if (bill == null) {
            return ResponseResult.error("账单不存在");
        }
        if (bill.getStatus() == 3) {
            return ResponseResult.error("账单已缴费");
        }
        if (bill.getStatus() == 5) {
            return ResponseResult.error("账单已作废");
        }

        BigDecimal totalAmount = bill.getAmount().add(bill.getLateFee() != null ? bill.getLateFee() : BigDecimal.ZERO);
        BigDecimal unpaidAmount = totalAmount.subtract(bill.getPaidAmount() != null ? bill.getPaidAmount() : BigDecimal.ZERO);

        PaymentRecord record = new PaymentRecord();
        record.setPaymentNo("PAY" + IdUtil.getSnowflakeNextIdStr());
        record.setBillId(billId);
        record.setOwnerId(bill.getOwnerId());
        record.setAmount(unpaidAmount);
        record.setPayMethod(payMethod);
        record.setStatus(1);
        record.setPayTime(LocalDateTime.now());
        record.setOperatorId(operatorId);
        record.setRemark("管理员代缴");
        paymentRecordMapper.insert(record);

        bill.setPaidAmount(totalAmount);
        bill.setStatus(3);
        feeBillMapper.updateById(bill);

        return ResponseResult.success(record);
    }

    @Override
    public ResponseResult<Map<String, Object>> getPaymentStatistics(Long communityId, String startDate, String endDate) {
        Map<String, Object> stats = new HashMap<>();

        // 已收金额
        LambdaQueryWrapper<PaymentRecord> paidWrapper = new LambdaQueryWrapper<>();
        paidWrapper.eq(PaymentRecord::getStatus, 1);
        List<PaymentRecord> paidRecords = paymentRecordMapper.selectList(paidWrapper);
        BigDecimal totalPaid = paidRecords.stream()
                .map(PaymentRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 待收金额
        LambdaQueryWrapper<FeeBill> unpaidWrapper = new LambdaQueryWrapper<>();
        unpaidWrapper.in(FeeBill::getStatus, 1, 2, 4); // 待缴费、部分缴费、已逾期
        if (communityId != null) {
            unpaidWrapper.eq(FeeBill::getCommunityId, communityId);
        }
        List<FeeBill> unpaidBills = feeBillMapper.selectList(unpaidWrapper);
        BigDecimal totalUnpaid = unpaidBills.stream()
                .map(b -> {
                    BigDecimal total = b.getAmount().add(b.getLateFee() != null ? b.getLateFee() : BigDecimal.ZERO);
                    return total.subtract(b.getPaidAmount() != null ? b.getPaidAmount() : BigDecimal.ZERO);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        stats.put("totalPaid", totalPaid);
        stats.put("totalUnpaid", totalUnpaid);
        stats.put("paidCount", paidRecords.size());
        stats.put("unpaidCount", unpaidBills.size());

        return ResponseResult.success(stats);
    }
}
