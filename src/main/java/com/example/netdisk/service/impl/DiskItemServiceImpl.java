package com.example.netdisk.service.impl;

import com.example.netdisk.entity.DiskItem;
import com.example.netdisk.exception.BusinessException;
import com.example.netdisk.mapper.DiskItemMapper;
import com.example.netdisk.service.DiskItemService;
import com.example.netdisk.service.MinioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiskItemServiceImpl implements DiskItemService {

    private final DiskItemMapper diskItemMapper;

    public DiskItemServiceImpl(DiskItemMapper diskItemMapper) {
        this.diskItemMapper = diskItemMapper;
    }

    @Override
    public List<DiskItem> list(Long ownerId, Long parentId) {
        return diskItemMapper.findByParent(ownerId, parentId);
    }

    // 创建文件夹
    @Override
    public DiskItem createFolder(Long ownerId, String name, Long parentId) {

        DiskItem folder= new DiskItem();
        folder.setType("FOLDER");
        folder.setName(name);
        folder.setParentId(parentId);
        folder.setOwnerId(ownerId);
        folder.setIsDeleted(false);

        diskItemMapper.insert(folder);
        return folder;
    }

    // 上传文件
    @Override
    public DiskItem saveFile(DiskItem file) {

        file.setType("FILE");
        file.setIsDeleted(false);

        diskItemMapper.insert(file);
        return file;
    }

    @Override
    public void move(Long ownerId, Long itemId, Long targetParentId) {
        DiskItem item = getById(ownerId, itemId);
        diskItemMapper.updateParent(item.getId(), targetParentId);
    }

    @Override
    public void delete(Long ownerId, Long itemId) {
        // 并不是多此一举，是为了确保删除的是自己的文件
        DiskItem item = getById(ownerId, itemId);
        diskItemMapper.softDelete(item.getId());
    }

    @Override
    public DiskItem getById(Long ownerId, Long id) {

        DiskItem item = diskItemMapper.findById(id);

        // 不能访问别人的文件
        if(item == null || !item.getOwnerId().equals(ownerId)) {
            throw new BusinessException(404, "资源不存在");
        }
        return item;
    }

}
