package com.example.netdisk.dto.FileInfo;

// 在「编译期」帮你自动生成 Java 代码的工具
import lombok.Data;

/**
 * 文件上传请求DTO
 */
@Data
public class UploadUrlRequest {
    private String originalName;
    private String contentType;
    private Long size;

    // Lombok 在编译时自动生成所有 getter、setter、equals、hashCode、toString 方法
}
