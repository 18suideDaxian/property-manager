package com.property.management.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.RepairOrder;
import com.property.management.mapper.RepairOrderMapper;
import com.property.management.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RepairServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements RepairService {

    @Autowired
    private RepairOrderMapper repairOrderMapper;

    @Override
    public ResponseResult<String> createRepairOrder(RepairOrder repairOrder) {
        repairOrder.setOrderNo("RPR" + IdUtil.getSnowflakeNextIdStr());
        repairOrder.setStatus(1); // 待受理
        repairOrderMapper.insert(repairOrder);
        return ResponseResult.success("报修提交成功，工单编号：" + repairOrder.getOrderNo());
    }

    @Override
    public ResponseResult<List<RepairOrder>> getOwnerRepairOrders(Long ownerId, Integer status, Integer page, Integer size) {
        Page<RepairOrder> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairOrder::getOwnerId, ownerId);
        if (status != null) {
            wrapper.eq(RepairOrder::getStatus, status);
        }
        wrapper.orderByDesc(RepairOrder::getCreateTime);
        repairOrderMapper.selectPage(pageParam, wrapper);
        return ResponseResult.success(pageParam.getRecords());
    }

    @Override
    public ResponseResult<RepairOrder> getOrderDetail(Long orderId, Long ownerId) {
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairOrder::getId, orderId).eq(RepairOrder::getOwnerId, ownerId);
        RepairOrder order = repairOrderMapper.selectOne(wrapper);
        if (order == null) {
            return ResponseResult.error("工单不存在");
        }
        return ResponseResult.success(order);
    }

    @Override
    public ResponseResult<String> rateOrder(Long orderId, Long ownerId, Integer rating, String content) {
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairOrder::getId, orderId).eq(RepairOrder::getOwnerId, ownerId);
        RepairOrder order = repairOrderMapper.selectOne(wrapper);
        if (order == null) {
            return ResponseResult.error("工单不存在");
        }
        if (order.getStatus() != 4) {
            return ResponseResult.error("只能评价已完成的工单");
        }
        order.setRating(rating);
        order.setRatingContent(content);
        order.setStatus(5); // 已评价
        repairOrderMapper.updateById(order);
        return ResponseResult.success("评价成功");
    }

    @Override
    public ResponseResult<String> cancelOrder(Long orderId, Long ownerId) {
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairOrder::getId, orderId).eq(RepairOrder::getOwnerId, ownerId);
        RepairOrder order = repairOrderMapper.selectOne(wrapper);
        if (order == null) {
            return ResponseResult.error("工单不存在");
        }
        if (order.getStatus() != 1) {
            return ResponseResult.error("只能取消待受理的工单");
        }
        order.setStatus(6); // 已关闭
        repairOrderMapper.updateById(order);
        return ResponseResult.success("工单已取消");
    }

    @Override
    public ResponseResult<List<RepairOrder>> getAllOrders(Long communityId, Long repairTypeId, Integer status, Integer urgencyLevel, Integer page, Integer size) {
        Page<RepairOrder> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        if (communityId != null) {
            wrapper.eq(RepairOrder::getCommunityId, communityId);
        }
        if (repairTypeId != null) {
            wrapper.eq(RepairOrder::getRepairTypeId, repairTypeId);
        }
        if (status != null) {
            wrapper.eq(RepairOrder::getStatus, status);
        }
        if (urgencyLevel != null) {
            wrapper.eq(RepairOrder::getUrgencyLevel, urgencyLevel);
        }
        wrapper.orderByAsc(RepairOrder::getUrgencyLevel).orderByDesc(RepairOrder::getCreateTime);
        repairOrderMapper.selectPage(pageParam, wrapper);
        return ResponseResult.success(pageParam.getRecords());
    }

    @Override
    public ResponseResult<String> acceptOrder(Long orderId, Long handlerId) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            return ResponseResult.error("工单不存在");
        }
        if (order.getStatus() != 1) {
            return ResponseResult.error("只能受理待受理的工单");
        }
        order.setStatus(2); // 已受理
        order.setHandlerId(handlerId);
        order.setHandleTime(LocalDateTime.now());
        repairOrderMapper.updateById(order);
        return ResponseResult.success("工单已受理");
    }

    @Override
    public ResponseResult<String> processOrder(Long orderId, Long handlerId, String remark) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            return ResponseResult.error("工单不存在");
        }
        if (order.getStatus() != 2) {
            return ResponseResult.error("只能处理已受理的工单");
        }
        order.setStatus(3); // 维修中
        order.setHandleRemark(remark);
        repairOrderMapper.updateById(order);
        return ResponseResult.success("工单处理中");
    }

    @Override
    public ResponseResult<String> completeOrder(Long orderId, Long handlerId, String remark) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            return ResponseResult.error("工单不存在");
        }
        if (order.getStatus() != 3) {
            return ResponseResult.error("只能完成维修中的工单");
        }
        order.setStatus(4); // 已完成
        order.setCompleteTime(LocalDateTime.now());
        if (remark != null && !remark.isEmpty()) {
            order.setHandleRemark(remark);
        }
        repairOrderMapper.updateById(order);
        return ResponseResult.success("工单已完成");
    }

    @Override
    public ResponseResult<String> closeOrder(Long orderId, Long handlerId) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            return ResponseResult.error("工单不存在");
        }
        order.setStatus(6); // 已关闭
        repairOrderMapper.updateById(order);
        return ResponseResult.success("工单已关闭");
    }

    @Override
    public ResponseResult<Map<String, Object>> getRepairStatistics(Long communityId) {
        Map<String, Object> stats = new HashMap<>();

        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        if (communityId != null) {
            wrapper.eq(RepairOrder::getCommunityId, communityId);
        }

        // 各状态统计
        wrapper.eq(RepairOrder::getStatus, 1);
        long pendingCount = repairOrderMapper.selectCount(wrapper);

        wrapper = new LambdaQueryWrapper<>();
        if (communityId != null) wrapper.eq(RepairOrder::getCommunityId, communityId);
        wrapper.in(RepairOrder::getStatus, 2, 3);
        long processingCount = repairOrderMapper.selectCount(wrapper);

        wrapper = new LambdaQueryWrapper<>();
        if (communityId != null) wrapper.eq(RepairOrder::getCommunityId, communityId);
        wrapper.in(RepairOrder::getStatus, 4, 5);
        long completedCount = repairOrderMapper.selectCount(wrapper);

        stats.put("pendingCount", pendingCount);
        stats.put("processingCount", processingCount);
        stats.put("completedCount", completedCount);
        stats.put("totalCount", pendingCount + processingCount + completedCount);

        return ResponseResult.success(stats);
    }
}
