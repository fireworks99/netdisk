package com.example.netdisk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file.upload")//读取配置文件中file.upload选项
public class FileUploadProperties {

    private String basePath;//file.upload.base-path=D:/NetDisk (basePath ↔ base-path 自动映射)

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
