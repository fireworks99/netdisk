package com.example.netdisk.service.impl;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.entity.DiskItem;
import com.example.netdisk.exception.BusinessException;
import com.example.netdisk.mapper.DiskItemMapper;
import com.example.netdisk.mapper.FavoriteMapper;
import com.example.netdisk.service.FavoriteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final DiskItemMapper diskItemMapper;

    public FavoriteServiceImpl(FavoriteMapper favoriteMapper, DiskItemMapper diskItemMapper) {
        this.favoriteMapper = favoriteMapper;
        this.diskItemMapper = diskItemMapper;
    }

    @Override
    public void add(Long userId, Long diskItemId) {
        DiskItem item = diskItemMapper.findById(diskItemId);
        if (item == null || !item.getOwnerId().equals(userId)) {
            throw new BusinessException(404, "文件不存在");
        }

        favoriteMapper.insert(userId, diskItemId);
    }

    @Override
    public void remove(Long userId, Long diskItemId) {
        favoriteMapper.delete(userId, diskItemId);
    }

    @Override
    public boolean exists(Long userId, Long diskItemId) {
        return favoriteMapper.exists(userId, diskItemId) > 0;
    }

    @Override
    public PageResult<DiskItem> page(Long userId, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;

        List<DiskItem> list = favoriteMapper.pageFavorite(userId, offset, pageSize);
        long total = favoriteMapper.countFavorite(userId);

        return new PageResult<>(total, list);
    }
}
