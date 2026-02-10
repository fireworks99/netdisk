package com.example.netdisk.job;

import com.example.netdisk.config.RecycleCleanProperties;
import com.example.netdisk.mapper.DiskItemMapper;
import com.example.netdisk.service.DiskItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RecycleBinCleanJob {

    private final DiskItemService diskItemService;
    private final DiskItemMapper diskItemMapper;
    private final RecycleCleanProperties properties;

    public RecycleBinCleanJob(DiskItemService diskItemService,
                              DiskItemMapper diskItemMapper,
                              RecycleCleanProperties properties) {
        this.diskItemService = diskItemService;
        this.diskItemMapper = diskItemMapper;
        this.properties = properties;
    }

    /**
     * 每天凌晨 3 点执行
     */
//    @Scheduled(cron = "0 0 3 * * ?")
    @Scheduled(cron = "#{@recycleCleanProperties.cron}")
    public void cleanRecycleBin() {

        Integer expireDays = properties.getExpireDays();
        if (expireDays == null || expireDays <= 0) {
            log.warn("回收站清理未执行：expire-days 未配置或非法");
            return;
        }

        log.info("开始清理回收站");

        List<Long> expiredIds = diskItemMapper.findExpiredDeleted(expireDays);

        log.info(expiredIds.toString());

        for (Long id : expiredIds) {
            try {
                // ownerId 可从 item 中查，这里建议 service 内部处理
                diskItemService.deleteForeverBySystem(id);
            } catch (Exception e) {
                log.error("清理失败，itemId={}", id, e);
            }
        }

        log.info("回收站清理完成，共 {} 条", expiredIds.size());
    }

}
