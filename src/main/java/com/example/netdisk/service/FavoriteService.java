package com.example.netdisk.service;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.entity.DiskItem;

public interface FavoriteService {

    void add(Long userId, Long diskItemId);

    void remove(Long userId, Long diskItemId);

    boolean exists(Long userId, Long diskItemId);

    PageResult<DiskItem> page(Long userId, int page, int pageSize);
}