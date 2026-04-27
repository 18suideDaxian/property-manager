package com.property.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.Building;
import com.property.management.entity.Community;
import com.property.management.entity.Unit;

import java.util.List;

public interface CommunityService extends IService<Community> {

    /**
     * 查询小区列表（业主可见的）
     */
    ResponseResult<List<Community>> getCommunityList(Integer status);

    /**
     * 查询小区详情
     */
    ResponseResult<Community> getCommunityDetail(Long communityId);

    /**
     * 管理员创建小区
     */
    ResponseResult<String> createCommunity(Community community);

    /**
     * 管理员修改小区信息
     */
    ResponseResult<String> updateCommunity(Community community);

    /**
     * 管理员删除小区
     */
    ResponseResult<String> deleteCommunity(Long communityId);

    /**
     * 管理员查询所有小区
     */
    ResponseResult<List<Community>> getAllCommunities(Integer status, Integer page, Integer size);

    /**
     * 查询楼栋列表
     */
    ResponseResult<List<Building>> getBuildings(Long communityId);

    /**
     * 管理员添加楼栋
     */
    ResponseResult<String> createBuilding(Building building);

    /**
     * 管理员修改楼栋
     */
    ResponseResult<String> updateBuilding(Building building);

    /**
     * 管理员删除楼栋
     */
    ResponseResult<String> deleteBuilding(Long buildingId);

    /**
     * 查询单元列表
     */
    ResponseResult<List<Unit>> getUnits(Long buildingId);

    /**
     * 管理员添加单元
     */
    ResponseResult<String> createUnit(Unit unit);

    /**
     * 管理员修改单元
     */
    ResponseResult<String> updateUnit(Unit unit);

    /**
     * 管理员删除单元
     */
    ResponseResult<String> deleteUnit(Long unitId);
}
