package com.property.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.Room;

import java.util.List;
import java.util.Map;

public interface RoomService extends IService<Room> {

    /**
     * 业主查询自己的房产列表
     */
    ResponseResult<List<Map<String, Object>>> getOwnerRooms(Long ownerId);

    /**
     * 业主查询房产详情
     */
    ResponseResult<Map<String, Object>> getRoomDetail(Long roomId, Long ownerId);

    /**
     * 管理员查询所有房产
     */
    ResponseResult<List<Room>> getAllRooms(Long communityId, Long buildingId, Long unitId, Integer roomType, Integer status, Integer page, Integer size);

    /**
     * 管理员添加房产
     */
    ResponseResult<String> createRoom(Room room);

    /**
     * 管理员修改房产
     */
    ResponseResult<String> updateRoom(Room room);

    /**
     * 管理员删除房产
     */
    ResponseResult<String> deleteRoom(Long roomId);

    /**
     * 管理员绑定业主到房产
     */
    ResponseResult<String> bindOwner(Long roomId, Long ownerId, Integer relationType);

    /**
     * 管理员解绑业主与房产
     */
    ResponseResult<String> unbindOwner(Long roomId, Long ownerId);
}
