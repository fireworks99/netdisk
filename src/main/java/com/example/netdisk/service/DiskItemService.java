package com.example.netdisk.service;

import com.example.netdisk.entity.DiskItem;

import java.util.List;

public interface DiskItemService {

    List<DiskItem> list(Long ownerId, Long parentId);

    DiskItem createFolder(Long ownerId, String name, Long parentId);

    DiskItem saveFile(DiskItem item);

    void move(Long ownerId, Long itemId, Long targetParentId);

    void delete(Long ownerId, Long itemId);

    DiskItem getById(Long ownerId, Long id);

}
