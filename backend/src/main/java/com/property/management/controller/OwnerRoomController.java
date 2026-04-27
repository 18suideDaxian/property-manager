package com.property.management.controller;

import com.property.management.dto.ResponseResult;
import com.property.management.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 业主房产控制器
 */
@RestController
@RequestMapping("/owner/room")
@CrossOrigin
public class OwnerRoomController {

    @Autowired
    private RoomService roomService;

    /**
     * 查询我的房产列表
     */
    @GetMapping("/list")
    public ResponseResult<List<Map<String, Object>>> getMyRooms(
            @RequestAttribute("userId") Long ownerId) {
        return roomService.getOwnerRooms(ownerId);
    }

    /**
     * 查询房产详情
     */
    @GetMapping("/{roomId}")
    public ResponseResult<Map<String, Object>> getRoomDetail(
            @RequestAttribute("userId") Long ownerId,
            @PathVariable Long roomId) {
        return roomService.getRoomDetail(roomId, ownerId);
    }
}
