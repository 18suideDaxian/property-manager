package com.property.management.controller;

import com.property.management.dto.ResponseResult;
import com.property.management.entity.RepairOrder;
import com.property.management.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 业主报修控制器
 */
@RestController
@RequestMapping("/owner/repair")
@CrossOrigin
public class OwnerRepairController {

    @Autowired
    private RepairService repairService;

    /**
     * 提交报修
     */
    @PostMapping("/orders")
    public ResponseResult<String> createRepairOrder(
            @RequestAttribute("userId") Long ownerId,
            @RequestBody RepairOrder repairOrder) {
        repairOrder.setOwnerId(ownerId);
        return repairService.createRepairOrder(repairOrder);
    }

    /**
     * 查询我的报修工单
     */
    @GetMapping("/orders")
    public ResponseResult<List<RepairOrder>> getMyRepairOrders(
            @RequestAttribute("userId") Long ownerId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return repairService.getOwnerRepairOrders(ownerId, status, page, size);
    }

    /**
     * 查询工单详情
     */
    @GetMapping("/orders/{orderId}")
    public ResponseResult<RepairOrder> getOrderDetail(
            @RequestAttribute("userId") Long ownerId,
            @PathVariable Long orderId) {
        return repairService.getOrderDetail(orderId, ownerId);
    }

    /**
     * 评价工单
     */
    @PostMapping("/orders/{orderId}/rate")
    public ResponseResult<String> rateOrder(
            @RequestAttribute("userId") Long ownerId,
            @PathVariable Long orderId,
            @RequestParam Integer rating,
            @RequestParam(required = false) String content) {
        return repairService.rateOrder(orderId, ownerId, rating, content);
    }

    /**
     * 取消工单
     */
    @PostMapping("/orders/{orderId}/cancel")
    public ResponseResult<String> cancelOrder(
            @RequestAttribute("userId") Long ownerId,
            @PathVariable Long orderId) {
        return repairService.cancelOrder(orderId, ownerId);
    }
}
