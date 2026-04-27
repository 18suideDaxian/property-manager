package com.property.management.controller;

import com.property.management.dto.ResponseResult;
import com.property.management.entity.Announcement;
import com.property.management.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员-公告管理控制器
 */
@RestController
@RequestMapping("/admin/announcement")
@CrossOrigin
public class AdminAnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 查询所有公告（含草稿）
     */
    @GetMapping("/list")
    public ResponseResult<List<Announcement>> getAllAnnouncements(
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return announcementService.getAllAnnouncements(communityId, status, type, page, size);
    }

    /**
     * 创建公告
     */
    @PostMapping("")
    public ResponseResult<String> createAnnouncement(@RequestBody Announcement announcement) {
        return announcementService.createAnnouncement(announcement);
    }

    /**
     * 修改公告
     */
    @PutMapping("/{announcementId}")
    public ResponseResult<String> updateAnnouncement(
            @PathVariable Long announcementId,
            @RequestBody Announcement announcement) {
        announcement.setId(announcementId);
        return announcementService.updateAnnouncement(announcement);
    }

    /**
     * 发布公告
     */
    @PostMapping("/{announcementId}/publish")
    public ResponseResult<String> publishAnnouncement(
            @PathVariable Long announcementId,
            @RequestAttribute("userId") Long publisherId,
            @RequestAttribute("username") String publisherName) {
        return announcementService.publishAnnouncement(announcementId, publisherId, publisherName);
    }

    /**
     * 撤回公告
     */
    @PostMapping("/{announcementId}/revoke")
    public ResponseResult<String> revokeAnnouncement(@PathVariable Long announcementId) {
        return announcementService.revokeAnnouncement(announcementId);
    }

    /**
     * 删除公告
     */
    @DeleteMapping("/{announcementId}")
    public ResponseResult<String> deleteAnnouncement(@PathVariable Long announcementId) {
        return announcementService.deleteAnnouncement(announcementId);
    }
}
