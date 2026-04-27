package com.property.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.Building;
import com.property.management.entity.OwnerRoom;
import com.property.management.entity.Room;
import com.property.management.entity.Unit;
import com.property.management.mapper.BuildingMapper;
import com.property.management.mapper.OwnerRoomMapper;
import com.property.management.mapper.RoomMapper;
import com.property.management.mapper.UnitMapper;
import com.property.management.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private OwnerRoomMapper ownerRoomMapper;

    @Autowired
    private BuildingMapper buildingMapper;

    @Autowired
    private UnitMapper unitMapper;

    @Override
    public ResponseResult<List<Map<String, Object>>> getOwnerRooms(Long ownerId) {
        // 查询业主关联的房产
        LambdaQueryWrapper<OwnerRoom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OwnerRoom::getOwnerId, ownerId).eq(OwnerRoom::getStatus, 1);
        List<OwnerRoom> ownerRooms = ownerRoomMapper.selectList(wrapper);

        List<Map<String, Object>> result = new ArrayList<>();
        for (OwnerRoom ownerRoom : ownerRooms) {
            Room room = roomMapper.selectById(ownerRoom.getRoomId());
            if (room != null) {
                Map<String, Object> roomInfo = new HashMap<>();
                roomInfo.put("roomId", room.getId());
                roomInfo.put("roomNo", room.getRoomNo());
                roomInfo.put("floor", room.getFloor());
                roomInfo.put("buildingArea", room.getBuildingArea());
                roomInfo.put("roomType", room.getRoomType());
                roomInfo.put("relationType", ownerRoom.getRelationType());
                roomInfo.put("isPrimary", ownerRoom.getIsPrimary());

                // 查询楼栋和单元信息
                Building building = buildingMapper.selectById(room.getBuildingId());
                if (building != null) {
                    roomInfo.put("buildingName", building.getName());
                }
                if (room.getUnitId() != null) {
                    Unit unit = unitMapper.selectById(room.getUnitId());
                    if (unit != null) {
                        roomInfo.put("unitName", unit.getName());
                    }
                }

                result.add(roomInfo);
            }
        }
        return ResponseResult.success(result);
    }

    @Override
    public ResponseResult<Map<String, Object>> getRoomDetail(Long roomId, Long ownerId) {
        // 验证业主是否拥有该房产
        LambdaQueryWrapper<OwnerRoom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OwnerRoom::getOwnerId, ownerId).eq(OwnerRoom::getRoomId, roomId).eq(OwnerRoom::getStatus, 1);
        OwnerRoom ownerRoom = ownerRoomMapper.selectOne(wrapper);
        if (ownerRoom == null) {
            return ResponseResult.error("您没有该房产的权限");
        }

        Room room = roomMapper.selectById(roomId);
        if (room == null) {
            return ResponseResult.error("房产不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("room", room);
        result.put("relationType", ownerRoom.getRelationType());
        result.put("isPrimary", ownerRoom.getIsPrimary());
        result.put("checkInTime", ownerRoom.getCheckInTime());

        // 查询小区信息
        Building building = buildingMapper.selectById(room.getBuildingId());
        if (building != null) {
            result.put("buildingName", building.getName());
        }
        if (room.getUnitId() != null) {
            Unit unit = unitMapper.selectById(room.getUnitId());
            if (unit != null) {
                result.put("unitName", unit.getName());
            }
        }

        return ResponseResult.success(result);
    }

    @Override
    public ResponseResult<List<Room>> getAllRooms(Long communityId, Long buildingId, Long unitId, Integer roomType, Integer status, Integer page, Integer size) {
        Page<Room> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        if (communityId != null) {
            wrapper.eq(Room::getCommunityId, communityId);
        }
        if (buildingId != null) {
            wrapper.eq(Room::getBuildingId, buildingId);
        }
        if (unitId != null) {
            wrapper.eq(Room::getUnitId, unitId);
        }
        if (roomType != null) {
            wrapper.eq(Room::getRoomType, roomType);
        }
        if (status != null) {
            wrapper.eq(Room::getStatus, status);
        }
        wrapper.orderByAsc(Room::getBuildingId).orderByAsc(Room::getFloor).orderByAsc(Room::getRoomNo);
        roomMapper.selectPage(pageParam, wrapper);
        return ResponseResult.success(pageParam.getRecords());
    }

    @Override
    public ResponseResult<String> createRoom(Room room) {
        roomMapper.insert(room);
        return ResponseResult.success("房产添加成功");
    }

    @Override
    public ResponseResult<String> updateRoom(Room room) {
        Room existing = roomMapper.selectById(room.getId());
        if (existing == null) {
            return ResponseResult.error("房产不存在");
        }
        roomMapper.updateById(room);
        return ResponseResult.success("房产修改成功");
    }

    @Override
    @Transactional
    public ResponseResult<String> deleteRoom(Long roomId) {
        Room room = roomMapper.selectById(roomId);
        if (room == null) {
            return ResponseResult.error("房产不存在");
        }
        // 检查是否有关联的业主
        LambdaQueryWrapper<OwnerRoom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OwnerRoom::getRoomId, roomId).eq(OwnerRoom::getStatus, 1);
        if (ownerRoomMapper.selectCount(wrapper) > 0) {
            return ResponseResult.error("该房产还有关联的业主，不能删除");
        }
        roomMapper.deleteById(roomId);
        return ResponseResult.success("房产删除成功");
    }

    @Override
    public ResponseResult<String> bindOwner(Long roomId, Long ownerId, Integer relationType) {
        // 检查是否已绑定
        LambdaQueryWrapper<OwnerRoom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OwnerRoom::getRoomId, roomId).eq(OwnerRoom::getOwnerId, ownerId).eq(OwnerRoom::getStatus, 1);
        if (ownerRoomMapper.selectCount(wrapper) > 0) {
            return ResponseResult.error("该业主已绑定此房产");
        }

        OwnerRoom ownerRoom = new OwnerRoom();
        ownerRoom.setRoomId(roomId);
        ownerRoom.setOwnerId(ownerId);
        ownerRoom.setRelationType(relationType);
        ownerRoom.setIsPrimary(0);
        ownerRoom.setStatus(1);
        ownerRoomMapper.insert(ownerRoom);
        return ResponseResult.success("绑定成功");
    }

    @Override
    public ResponseResult<String> unbindOwner(Long roomId, Long ownerId) {
        LambdaQueryWrapper<OwnerRoom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OwnerRoom::getRoomId, roomId).eq(OwnerRoom::getOwnerId, ownerId).eq(OwnerRoom::getStatus, 1);
        OwnerRoom ownerRoom = ownerRoomMapper.selectOne(wrapper);
        if (ownerRoom == null) {
            return ResponseResult.error("绑定关系不存在");
        }
        ownerRoom.setStatus(0);
        ownerRoomMapper.updateById(ownerRoom);
        return ResponseResult.success("解绑成功");
    }
}
