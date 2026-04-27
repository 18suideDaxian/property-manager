package com.property.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.dto.ResponseResult;
import com.property.management.entity.Announcement;

import java.util.List;

public interface AnnouncementService extends IService<Announcement> {

    /**
     * 业主查询公告列表（已发布的）
     */
    ResponseResult<List<Announcement>> getPublishedAnnouncements(Long communityId, Integer type, Integer page, Integer size);

    /**
     * 业主查询公告详情
     */
    ResponseResult<Announcement> getAnnouncementDetail(Long announcementId);

    /**
     * 管理员创建公告
     */
    ResponseResult<String> createAnnouncement(Announcement announcement);

    /**
     * 管理员修改公告
     */
    ResponseResult<String> updateAnnouncement(Announcement announcement);

    /**
     * 管理员发布公告
     */
    ResponseResult<String> publishAnnouncement(Long announcementId, Long publisherId, String publisherName);

    /**
     * 管理员撤回公告
     */
    ResponseResult<String> revokeAnnouncement(Long announcementId);

    /**
     * 管理员删除公告
     */
    ResponseResult<String> deleteAnnouncement(Long announcementId);

    /**
     * 管理员查询所有公告（含草稿）
     */
    ResponseResult<List<Announcement>> getAllAnnouncements(Long communityId, Integer status, Integer type, Integer page, Integer size);
}
