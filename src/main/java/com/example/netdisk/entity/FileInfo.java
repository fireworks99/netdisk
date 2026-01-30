package com.example.netdisk.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileInfo {

    /** 主键 */
    private Long id;

    /** 原始文件名（用户看到的） */
    private String originalName;

    /** MinIO bucket （核心之一：minio://bucketName/objectKey）*/
    private String bucketName;

    /** 对象名（MinIO objectKey / objectName）（核心之一：minio://bucketName/objectKey） */
    private String objectKey;

    /** 文件大小（字节） */
    private Long fileSize;

    /** MIME 类型 */
    private String contentType;

    /** 文件后缀 */
    private String fileExt;

    /** 上传者（可选） */
    private Long uploaderId;

    /** MinIO 返回的 etag（可用于校验） */
    private String etag;

    /** 是否公开访问（public / private） */
    private Boolean isPublic;

    /** 创建时间 */
    private LocalDateTime createTime;

}
