package com.example.netdisk.service;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.entity.DiskItem;

public interface RecentService {

    void record(Long userId, Long diskItemId, String action);

    PageResult<DiskItem> page(Long userId, int page, int pageSize);

}
