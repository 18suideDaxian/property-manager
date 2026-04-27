package com.property.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.Building;
import com.property.management.entity.Community;
import com.property.management.entity.Unit;
import com.property.management.mapper.BuildingMapper;
import com.property.management.mapper.CommunityMapper;
import com.property.management.mapper.UnitMapper;
import com.property.management.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {

    @Autowired
    private CommunityMapper communityMapper;

    @Autowired
    private BuildingMapper buildingMapper;

    @Autowired
    private UnitMapper unitMapper;

    @Override
    public ResponseResult<List<Community>> getCommunityList(Integer status) {
        LambdaQueryWrapper<Community> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Community::getStatus, status);
        }
        wrapper.orderByAsc(Community::getCreateTime);
        return ResponseResult.success(communityMapper.selectList(wrapper));
    }

    @Override
    public ResponseResult<Community> getCommunityDetail(Long communityId) {
        Community community = communityMapper.selectById(communityId);
        if (community == null) {
            return ResponseResult.error("小区不存在");
        }
        return ResponseResult.success(community);
    }

    @Override
    public ResponseResult<String> createCommunity(Community community) {
        community.setStatus(1);
        communityMapper.insert(community);
        return ResponseResult.success("小区创建成功");
    }

    @Override
    public ResponseResult<String> updateCommunity(Community community) {
        Community existing = communityMapper.selectById(community.getId());
        if (existing == null) {
            return ResponseResult.error("小区不存在");
        }
        communityMapper.updateById(community);
        return ResponseResult.success("小区信息修改成功");
    }

    @Override
    @Transactional
    public ResponseResult<String> deleteCommunity(Long communityId) {
        Community community = communityMapper.selectById(communityId);
        if (community == null) {
            return ResponseResult.error("小区不存在");
        }
        // 检查是否有关联的楼栋
        LambdaQueryWrapper<Building> buildingWrapper = new LambdaQueryWrapper<>();
        buildingWrapper.eq(Building::getCommunityId, communityId);
        if (buildingMapper.selectCount(buildingWrapper) > 0) {
            return ResponseResult.error("该小区下还有楼栋，不能删除");
        }
        communityMapper.deleteById(communityId);
        return ResponseResult.success("小区删除成功");
    }

    @Override
    public ResponseResult<List<Community>> getAllCommunities(Integer status, Integer page, Integer size) {
        Page<Community> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Community> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Community::getStatus, status);
        }
        wrapper.orderByAsc(Community::getCreateTime);
        communityMapper.selectPage(pageParam, wrapper);
        return ResponseResult.success(pageParam.getRecords());
    }

    @Override
    public ResponseResult<List<Building>> getBuildings(Long communityId) {
        LambdaQueryWrapper<Building> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Building::getCommunityId, communityId).eq(Building::getStatus, 1);
        wrapper.orderByAsc(Building::getCreateTime);
        return ResponseResult.success(buildingMapper.selectList(wrapper));
    }

    @Override
    public ResponseResult<String> createBuilding(Building building) {
        building.setStatus(1);
        buildingMapper.insert(building);
        return ResponseResult.success("楼栋添加成功");
    }

    @Override
    public ResponseResult<String> updateBuilding(Building building) {
        Building existing = buildingMapper.selectById(building.getId());
        if (existing == null) {
            return ResponseResult.error("楼栋不存在");
        }
        buildingMapper.updateById(building);
        return ResponseResult.success("楼栋信息修改成功");
    }

    @Override
    @Transactional
    public ResponseResult<String> deleteBuilding(Long buildingId) {
        Building building = buildingMapper.selectById(buildingId);
        if (building == null) {
            return ResponseResult.error("楼栋不存在");
        }
        // 检查是否有关联的单元
        LambdaQueryWrapper<Unit> unitWrapper = new LambdaQueryWrapper<>();
        unitWrapper.eq(Unit::getBuildingId, buildingId);
        if (unitMapper.selectCount(unitWrapper) > 0) {
            return ResponseResult.error("该楼栋下还有单元，不能删除");
        }
        buildingMapper.deleteById(buildingId);
        return ResponseResult.success("楼栋删除成功");
    }

    @Override
    public ResponseResult<List<Unit>> getUnits(Long buildingId) {
        LambdaQueryWrapper<Unit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Unit::getBuildingId, buildingId).eq(Unit::getStatus, 1);
        wrapper.orderByAsc(Unit::getCreateTime);
        return ResponseResult.success(unitMapper.selectList(wrapper));
    }

    @Override
    public ResponseResult<String> createUnit(Unit unit) {
        unit.setStatus(1);
        unitMapper.insert(unit);
        return ResponseResult.success("单元添加成功");
    }

    @Override
    public ResponseResult<String> updateUnit(Unit unit) {
        Unit existing = unitMapper.selectById(unit.getId());
        if (existing == null) {
            return ResponseResult.error("单元不存在");
        }
        unitMapper.updateById(unit);
        return ResponseResult.success("单元信息修改成功");
    }

    @Override
    @Transactional
    public ResponseResult<String> deleteUnit(Long unitId) {
        Unit unit = unitMapper.selectById(unitId);
        if (unit == null) {
            return ResponseResult.error("单元不存在");
        }
        unitMapper.deleteById(unitId);
        return ResponseResult.success("单元删除成功");
    }
}
