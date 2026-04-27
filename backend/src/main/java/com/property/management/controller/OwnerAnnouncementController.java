package com.property.management.controller;

import com.property.management.dto.ResponseResult;
import com.property.management.entity.Announcement;
import com.property.management.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 业主公告控制器
 */
@RestController
@RequestMapping("/owner/announcement")
@CrossOrigin
public class OwnerAnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 查询公告列表（已发布）
     */
    @GetMapping("/list")
    public ResponseResult<List<Announcement>> getAnnouncements(
            @RequestParam Long communityId,
            @RequestParam(required = false) Integer type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return announcementService.getPublishedAnnouncements(communityId, type, page, size);
    }

    /**
     * 查询公告详情
     */
    @GetMapping("/{announcementId}")
    public ResponseResult<Announcement> getAnnouncementDetail(
            @PathVariable Long announcementId) {
        return announcementService.getAnnouncementDetail(announcementId);
    }
}
