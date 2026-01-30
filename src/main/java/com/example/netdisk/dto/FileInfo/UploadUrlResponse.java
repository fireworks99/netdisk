package com.example.netdisk.dto.FileInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 返回预签名URL
 */
@Data
@AllArgsConstructor
public class UploadUrlResponse {
    private String uploadUrl;
    private String objectName;

    /**
     * @AllArgsConstructor: 为类自动生成一个包含所有字段的构造方法
     * public UploadUrlResponse(String uploadUrl, String objectName) {
     *     this.uploadUrl = uploadUrl;
     *     this.objectName = objectName;
     * }
     */
}
