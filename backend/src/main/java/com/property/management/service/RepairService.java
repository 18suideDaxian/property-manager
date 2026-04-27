package com.property.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.RepairOrder;

import java.util.List;
import java.util.Map;

public interface RepairService extends IService<RepairOrder> {

    /**
     * 业主提交报修
     */
    ResponseResult<String> createRepairOrder(RepairOrder repairOrder);

    /**
     * 业主查询自己的报修工单
     */
    ResponseResult<List<RepairOrder>> getOwnerRepairOrders(Long ownerId, Integer status, Integer page, Integer size);

    /**
     * 业主查询工单详情
     */
    ResponseResult<RepairOrder> getOrderDetail(Long orderId, Long ownerId);

    /**
     * 业主评价工单
     */
    ResponseResult<String> rateOrder(Long orderId, Long ownerId, Integer rating, String content);

    /**
     * 业主取消工单
     */
    ResponseResult<String> cancelOrder(Long orderId, Long ownerId);

    /**
     * 管理员查询所有工单
     */
    ResponseResult<List<RepairOrder>> getAllOrders(Long communityId, Long repairTypeId, Integer status, Integer urgencyLevel, Integer page, Integer size);

    /**
     * 管理员受理工单
     */
    ResponseResult<String> acceptOrder(Long orderId, Long handlerId);

    /**
     * 管理员处理工单（开始维修）
     */
    ResponseResult<String> processOrder(Long orderId, Long handlerId, String remark);

    /**
     * 管理员完成工单
     */
    ResponseResult<String> completeOrder(Long orderId, Long handlerId, String remark);

    /**
     * 管理员关闭工单
     */
    ResponseResult<String> closeOrder(Long orderId, Long handlerId);

    /**
     * 查询报修统计
     */
    ResponseResult<Map<String, Object>> getRepairStatistics(Long communityId);
}
