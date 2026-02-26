package com.example.netdisk.service.impl;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.entity.DiskItem;
import com.example.netdisk.mapper.RecentMapper;
import com.example.netdisk.service.RecentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentServiceImpl implements RecentService {

    private final RecentMapper recentMapper;

    @Override
    public void record(Long userId, Long diskItemId, String action) {
        recentMapper.insertOrUpdate(userId, diskItemId, action);
    }

    @Override
    public PageResult<DiskItem> page(Long userId, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;

        List<DiskItem> list = recentMapper.pageRecent(userId, offset, pageSize);
        long total = recentMapper.countRecent(userId);

        return new PageResult<>(total, list);
    }
}
