package com.example.netdisk.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DiskItem {
    private Long id;

    /** File / Folder */
    private String type;

    private String name;

    private Long parentId;
    private Long ownerId;

    // ---- 文件专属 ----
    private String bucketName;
    private String objectKey;
    private Long fileSize;
    private String contentType;
    private String fileExt;
    private String etag;

    // ---- 状态 ----
    private Boolean isDeleted;
    private LocalDateTime deletedAt;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
