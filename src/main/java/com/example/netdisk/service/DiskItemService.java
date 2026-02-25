package com.example.netdisk.service;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.dto.DiskItem.DiskItemQuery;
import com.example.netdisk.entity.DiskItem;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DiskItemService {

    List<DiskItem> list(Long ownerId, Long parentId);

    DiskItem createFolder(Long ownerId, String name, Long parentId);

    DiskItem saveFile(DiskItem item);

    List<DiskItem> batchSaveFile(List<DiskItem> files);

    void move(Long ownerId, Long itemId, Long targetParentId);

    void batchMove(Long ownerId, List<Long> ids, Long targetParentId);

    void delete(Long ownerId, Long itemId);

    void batchSoftDelete(Long ownerId, List<Long> ids);

    DiskItem getById(Long ownerId, Long id);

    List<DiskItem> listDeleted(Long ownerId);

    void restore(Long ownerId, Long itemId);

    void deleteForever(Long ownerId, Long itemId);

    void deleteForeverBySystem(Long itemId);

    PageResult<DiskItem> pageQuery(DiskItemQuery query);

    void batchDownload(Long ownerId, List<Long> ids, HttpServletResponse response) throws Exception;
}
