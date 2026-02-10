package com.example.netdisk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "disk.recycle")
public class RecycleCleanProperties {

    /**
     * 回收站保留天数
     */
    private Integer expireDays;

    /**
     * 定时任务 cron 表达式
     */
    private String cron;

}
