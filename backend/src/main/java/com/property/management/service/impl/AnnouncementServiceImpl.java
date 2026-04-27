package com.property.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.Announcement;
import com.property.management.mapper.AnnouncementMapper;
import com.property.management.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public ResponseResult<List<Announcement>> getPublishedAnnouncements(Long communityId, Integer type, Integer page, Integer size) {
        Page<Announcement> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getCommunityId, communityId);
        wrapper.eq(Announcement::getStatus, 2); // 已发布
        if (type != null) {
            wrapper.eq(Announcement::getType, type);
        }
        wrapper.orderByDesc(Announcement::getIsTop).orderByDesc(Announcement::getPublishTime);
        announcementMapper.selectPage(pageParam, wrapper);
        return ResponseResult.success(pageParam.getRecords());
    }

    @Override
    public ResponseResult<Announcement> getAnnouncementDetail(Long announcementId) {
        Announcement announcement = announcementMapper.selectById(announcementId);
        if (announcement == null) {
            return ResponseResult.error("公告不存在");
        }
        if (announcement.getStatus() != 2) {
            return ResponseResult.error("公告未发布或已撤回");
        }
        // 增加阅读次数
        announcement.setReadCount(announcement.getReadCount() != null ? announcement.getReadCount() + 1 : 1);
        announcementMapper.updateById(announcement);
        return ResponseResult.success(announcement);
    }

    @Override
    public ResponseResult<String> createAnnouncement(Announcement announcement) {
        announcement.setStatus(1); // 草稿
        announcement.setReadCount(0);
        announcement.setIsTop(announcement.getIsTop() != null ? announcement.getIsTop() : 0);
        announcementMapper.insert(announcement);
        return ResponseResult.success("公告创建成功");
    }

    @Override
    public ResponseResult<String> updateAnnouncement(Announcement announcement) {
        Announcement existing = announcementMapper.selectById(announcement.getId());
        if (existing == null) {
            return ResponseResult.error("公告不存在");
        }
        if (existing.getStatus() == 3) {
            return ResponseResult.error("已撤回的公告不能修改");
        }
        // 只允许修改草稿状态的公告
        if (existing.getStatus() != 1) {
            return ResponseResult.error("只能修改草稿状态的公告");
        }
        announcementMapper.updateById(announcement);
        return ResponseResult.success("公告修改成功");
    }

    @Override
    public ResponseResult<String> publishAnnouncement(Long announcementId, Long publisherId, String publisherName) {
        Announcement announcement = announcementMapper.selectById(announcementId);
        if (announcement == null) {
            return ResponseResult.error("公告不存在");
        }
        if (announcement.getStatus() != 1) {
            return ResponseResult.error("只能发布公告（草稿状态）");
        }
        announcement.setStatus(2); // 已发布
        announcement.setPublisherId(publisherId);
        announcement.setPublisherName(publisherName);
        announcement.setPublishTime(LocalDateTime.now());
        announcementMapper.updateById(announcement);
        return ResponseResult.success("公告发布成功");
    }

    @Override
    public ResponseResult<String> revokeAnnouncement(Long announcementId) {
        Announcement announcement = announcementMapper.selectById(announcementId);
        if (announcement == null) {
            return ResponseResult.error("公告不存在");
        }
        if (announcement.getStatus() != 2) {
            return ResponseResult.error("只能撤回已发布的公告");
        }
        announcement.setStatus(3); // 已撤回
        announcement.setRevokeTime(LocalDateTime.now());
        announcementMapper.updateById(announcement);
        return ResponseResult.success("公告已撤回");
    }

    @Override
    public ResponseResult<String> deleteAnnouncement(Long announcementId) {
        Announcement announcement = announcementMapper.selectById(announcementId);
        if (announcement == null) {
            return ResponseResult.error("公告不存在");
        }
        announcementMapper.deleteById(announcementId);
        return ResponseResult.success("公告已删除");
    }

    @Override
    public ResponseResult<List<Announcement>> getAllAnnouncements(Long communityId, Integer status, Integer type, Integer page, Integer size) {
        Page<Announcement> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        if (communityId != null) {
            wrapper.eq(Announcement::getCommunityId, communityId);
        }
        if (status != null) {
            wrapper.eq(Announcement::getStatus, status);
        }
        if (type != null) {
            wrapper.eq(Announcement::getType, type);
        }
        wrapper.orderByDesc(Announcement::getCreateTime);
        announcementMapper.selectPage(pageParam, wrapper);
        return ResponseResult.success(pageParam.getRecords());
    }
}
