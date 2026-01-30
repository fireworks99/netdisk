package com.example.netdisk.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * 构建 objectName(统一规范)
     * @param userId 用户ID
     * @param originalName 原名
     * @return objectName
     */
    public String buildObjectName(Long userId, String originalName) {
        String safeName = originalName.replaceAll("\\s+", "_");//将所有连续的空格字符替换为下划线
        return "user-" + userId + "/" + System.currentTimeMillis()  + "_" + safeName;
    }

    /**
     * 生成 预签名URL
     * @param objectName 对象名称
     * @param contentType 文件类型
     * @return 预签名URL
     * @throws Exception 异常
     */
    public String getUploadUrl(String objectName, String contentType) throws Exception {

        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucket)
                        .object(objectName)
                        .expiry(10, TimeUnit.MINUTES)
                        .extraQueryParams(
                                Collections.singletonMap("Content-Type", contentType)
                        )
                        .build()
        );
    }

    /**
     * 获取文件预览 / 访问 URL
     * @param objectName MinIO object key
     * @param contentType 文件类型
     */
    public String getPreviewUrl(String objectName, String contentType) {

        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(objectName)
                            .expiry(30, TimeUnit.MINUTES)
                            // 告诉浏览器这是啥类型（非常重要）
                            .extraQueryParams(
                                    Collections.singletonMap(
                                            "response-content-type",
                                            contentType
                                    )
                            )
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("获取文件预览URL失败", e);
        }
    }

}
