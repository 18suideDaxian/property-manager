package com.property.management.controller;

import com.property.management.dto.ResponseResult;
import com.property.management.entity.Building;
import com.property.management.entity.Community;
import com.property.management.entity.Unit;
import com.property.management.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员-小区管理控制器
 */
@RestController
@RequestMapping("/admin/community")
@CrossOrigin
public class AdminCommunityController {

    @Autowired
    private CommunityService communityService;

    // ========== 小区管理 ==========

    /**
     * 查询所有小区
     */
    @GetMapping("/list")
    public ResponseResult<List<Community>> getCommunityList(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return communityService.getAllCommunities(status, page, size);
    }

    /**
     * 查询小区详情
     */
    @GetMapping("/{communityId}")
    public ResponseResult<Community> getCommunityDetail(@PathVariable Long communityId) {
        return communityService.getCommunityDetail(communityId);
    }

    /**
     * 创建小区
     */
    @PostMapping("")
    public ResponseResult<String> createCommunity(@RequestBody Community community) {
        return communityService.createCommunity(community);
    }

    /**
     * 修改小区信息
     */
    @PutMapping("/{communityId}")
    public ResponseResult<String> updateCommunity(
            @PathVariable Long communityId,
            @RequestBody Community community) {
        community.setId(communityId);
        return communityService.updateCommunity(community);
    }

    /**
     * 删除小区
     */
    @DeleteMapping("/{communityId}")
    public ResponseResult<String> deleteCommunity(@PathVariable Long communityId) {
        return communityService.deleteCommunity(communityId);
    }

    // ========== 楼栋管理 ==========

    /**
     * 查询楼栋列表
     */
    @GetMapping("/{communityId}/buildings")
    public ResponseResult<List<Building>> getBuildings(@PathVariable Long communityId) {
        return communityService.getBuildings(communityId);
    }

    /**
     * 添加楼栋
     */
    @PostMapping("/buildings")
    public ResponseResult<String> createBuilding(@RequestBody Building building) {
        return communityService.createBuilding(building);
    }

    /**
     * 修改楼栋
     */
    @PutMapping("/buildings/{buildingId}")
    public ResponseResult<String> updateBuilding(
            @PathVariable Long buildingId,
            @RequestBody Building building) {
        building.setId(buildingId);
        return communityService.updateBuilding(building);
    }

    /**
     * 删除楼栋
     */
    @DeleteMapping("/buildings/{buildingId}")
    public ResponseResult<String> deleteBuilding(@PathVariable Long buildingId) {
        return communityService.deleteBuilding(buildingId);
    }

    // ========== 单元管理 ==========

    /**
     * 查询单元列表
     */
    @GetMapping("/buildings/{buildingId}/units")
    public ResponseResult<List<Unit>> getUnits(@PathVariable Long buildingId) {
        return communityService.getUnits(buildingId);
    }

    /**
     * 添加单元
     */
    @PostMapping("/units")
    public ResponseResult<String> createUnit(@RequestBody Unit unit) {
        return communityService.createUnit(unit);
    }

    /**
     * 修改单元
     */
    @PutMapping("/units/{unitId}")
    public ResponseResult<String> updateUnit(
            @PathVariable Long unitId,
            @RequestBody Unit unit) {
        unit.setId(unitId);
        return communityService.updateUnit(unit);
    }

    /**
     * 删除单元
     */
    @DeleteMapping("/units/{unitId}")
    public ResponseResult<String> deleteUnit(@PathVariable Long unitId) {
        return communityService.deleteUnit(unitId);
    }
}
