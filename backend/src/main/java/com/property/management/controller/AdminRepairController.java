package com.property.management.controller;

import com.property.management.dto.ResponseResult;
import com.property.management.entity.RepairOrder;
import com.property.management.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理员-报修工单控制器
 */
@RestController
@RequestMapping("/admin/repair")
@CrossOrigin
public class AdminRepairController {

    @Autowired
    private RepairService repairService;

    /**
     * 查询所有工单
     */
    @GetMapping("/orders")
    public ResponseResult<List<RepairOrder>> getAllOrders(
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) Long repairTypeId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer urgencyLevel,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return repairService.getAllOrders(communityId, repairTypeId, status, urgencyLevel, page, size);
    }

    /**
     * 受理工单
     */
    @PostMapping("/orders/{orderId}/accept")
    public ResponseResult<String> acceptOrder(
            @PathVariable Long orderId,
            @RequestAttribute("userId") Long handlerId) {
        return repairService.acceptOrder(orderId, handlerId);
    }

    /**
     * 处理工单（开始维修）
     */
    @PostMapping("/orders/{orderId}/process")
    public ResponseResult<String> processOrder(
            @PathVariable Long orderId,
            @RequestAttribute("userId") Long handlerId,
            @RequestParam(required = false) String remark) {
        return repairService.processOrder(orderId, handlerId, remark);
    }

    /**
     * 完成工单
     */
    @PostMapping("/orders/{orderId}/complete")
    public ResponseResult<String> completeOrder(
            @PathVariable Long orderId,
            @RequestAttribute("userId") Long handlerId,
            @RequestParam(required = false) String remark) {
        return repairService.completeOrder(orderId, handlerId, remark);
    }

    /**
     * 关闭工单
     */
    @PostMapping("/orders/{orderId}/close")
    public ResponseResult<String> closeOrder(
            @PathVariable Long orderId,
            @RequestAttribute("userId") Long handlerId) {
        return repairService.closeOrder(orderId, handlerId);
    }

    /**
     * 查询报修统计
     */
    @GetMapping("/statistics")
    public ResponseResult<Map<String, Object>> getRepairStatistics(
            @RequestParam(required = false) Long communityId) {
        return repairService.getRepairStatistics(communityId);
    }
}
