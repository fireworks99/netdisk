package com.example.netdisk.service;

import java.io.InputStream;

/**
 * MinIO 对象存储服务接口
 * 提供文件上传、下载、预览和删除等功能
 */
public interface MinioService {

    /**
     * 构建对象存储中的对象名称（统一规范）
     * @param userId 用户ID
     * @param originalName 原始文件名
     * @return 规范化的对象名称，格式：user-{userId}/{timestamp}_{sanitizedFileName}
     */
    String buildObjectName(Long userId, String originalName);

    /**
     * 生成文件上传的预签名URL
     * 客户端可以使用该URL直接上传文件到MinIO
     * @param objectName 对象名称
     * @param contentType 文件类型（MIME type）
     * @return 预签名上传URL，有效期10分钟
     * @throws Exception 生成URL过程中可能出现的异常
     */
    String getUploadUrl(String objectName, String contentType) throws Exception;

    /**
     * 获取文件预览/访问的预签名URL
     * 客户端可以使用该URL直接访问或预览文件
     * @param objectName MinIO对象名称
     * @param contentType 文件类型（MIME type）
     * @return 预签名访问URL，有效期30分钟
     * @throws RuntimeException 生成URL失败时抛出运行时异常
     */
    String getPreviewUrl(String objectName, String contentType);

    /**
     * 物理删除文件
     * @param bucket 存储桶名称
     * @param objectKey 对象键（文件路径）
     * @throws com.example.netdisk.exception.BusinessException 删除失败时抛出业务异常
     */
    void deleteObject(String bucket, String objectKey);

    /**
     * 获取文件对象的数据流
     * @param bucketName 存储桶名称
     * @param objectKey 对象键（文件路径）
     * @return 文件的输入流
     * @throws RuntimeException 获取文件失败时抛出运行时异常
     */
    InputStream getObject(String bucketName, String objectKey);

    /**
     * 删除对象存储中的文件
     * @param bucketName 存储桶名称
     * @param objectKey 对象键（文件路径）
     * @throws RuntimeException 删除失败时抛出运行时异常
     */
    void removeObject(String bucketName, String objectKey);
}
