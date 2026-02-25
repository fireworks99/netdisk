package com.example.netdisk.service.impl;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.dto.DiskItem.DiskItemQuery;
import com.example.netdisk.entity.DiskItem;
import com.example.netdisk.exception.BusinessException;
import com.example.netdisk.mapper.DiskItemMapper;
import com.example.netdisk.service.DiskItemService;
import com.example.netdisk.service.MinioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DiskItemServiceImpl implements DiskItemService {

    private final DiskItemMapper diskItemMapper;
    private final MinioService minioService;

    public DiskItemServiceImpl(DiskItemMapper diskItemMapper, MinioService minioService) {
        this.diskItemMapper = diskItemMapper;
        this.minioService = minioService;
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
    public List<DiskItem> batchSaveFile(List<DiskItem> files) {
        List<DiskItem> list = new ArrayList<>();
        for(DiskItem file: files) {
            file.setType("FILE");
            file.setIsDeleted(false);

            diskItemMapper.insert(file);
            list.add(file);
        }
        return list;
    }

    public boolean isChildOf(Long targetId, Long sourceId) {

        while (targetId != null) {
            DiskItem item = diskItemMapper.findById(targetId);
            if(item == null) {
                return false;
            }
            if(Objects.equals(item.getParentId(), sourceId)) {
                return true;
            }
            targetId = item.getParentId();
        }

        return false;
    }

    // 文件(夹)移动
    @Override
    public void move(Long ownerId, Long itemId, Long targetParentId) {
        DiskItem item = getById(ownerId, itemId);

        if(Objects.equals(itemId, targetParentId)) {
            throw new RuntimeException("不能移动到自身目录");
        }

        if(isChildOf(targetParentId, itemId)) {
            throw new RuntimeException("不能移动到子目录");
        }

        diskItemMapper.updateParent(item.getId(), targetParentId);
    }

    @Override
    @Transactional
    public void batchMove(Long ownerId, List<Long> ids, Long targetParentId) {
//        for(Long id: ids) {
//            move(ownerId, id, targetParentId);
//        }
        diskItemMapper.batchMove(ownerId, ids, targetParentId);
    }

    // 逻辑删除
    @Override
    public void delete(Long ownerId, Long itemId) {
        // 并不是多此一举，是为了确保删除的是自己的文件
        DiskItem item = getById(ownerId, itemId);
        diskItemMapper.softDelete(item.getId());
    }

    @Override
    @Transactional
    public void batchSoftDelete(Long ownerId, List<Long> ids) {
//        for(Long id: ids) {
//            delete(ownerId, id);
//        }
        diskItemMapper.batchSoftDelete(ownerId, ids);
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

    /**
     * 回收站列表
     */
    @Override
    public List<DiskItem> listDeleted(Long ownerId) {
        return diskItemMapper.findDeleted(ownerId);
    }

    /**
     * 恢复已删除的文件（夹）
     */
    @Override
    public void restore(Long ownerId, Long itemId) {

        DiskItem item = getById(ownerId, itemId);
        if(!Boolean.TRUE.equals(item.getIsDeleted())) {
            throw new BusinessException(400, "该资源不在回收站中");
        }
        diskItemMapper.restore(item.getId());

    }

    /**
     * 物理删除（文件夹递归删除）
     */
    @Override
    public void deleteForever(Long ownerId, Long itemId) {

        DiskItem item = getById(ownerId, itemId);
        if(!Boolean.TRUE.equals(item.getIsDeleted())) {
            throw new BusinessException(400, "该资源不在回收站中");
        }
        deleteRecursively(item);
    }

    public void deleteRecursively(DiskItem item) {

        // 如果是文件，先删除MinIO对象
        if("FILE".equals(item.getType())) {
            minioService.deleteObject(item.getBucketName(), item.getObjectKey());
        }

        // 查询子节点并删除（只有文件夹才有子节点，不然这里要放到删除的前面）
        List<DiskItem> children = diskItemMapper.findChildren(item.getId());
        for(DiskItem child: children) {
            deleteRecursively(child);
        }

        diskItemMapper.deletePhysical(item.getId());
    }

    /**
     * @ Transactional: 自动管理事务的开启、提交、回滚，让你不用手动编写事务代码。 "修改“操作最好加上这一注解
     */
    @Override
    @Transactional
    public void deleteForeverBySystem(Long itemId) {
        DiskItem item = diskItemMapper.findById(itemId);
        if (item == null) {
            return;
        }

        if (!Boolean.TRUE.equals(item.getIsDeleted())) {
            return;
        }

        deleteRecursively(item);
    }

    /**
     * 分页按条件查询
     */
    @Override
    public PageResult<DiskItem> pageQuery(DiskItemQuery query) {
        int offset = (query.getPageNum() - 1) * query.getPageSize();
        query.setOffset(offset);

        List<DiskItem> list = diskItemMapper.pageQuery(query);
        Long total = diskItemMapper.countQuery(query);

        return new PageResult<>(total, list);
    }

    @Override
    public void batchDownload(Long ownerId, List<Long> ids, HttpServletResponse response) throws Exception {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=download.zip");

        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

        try {
            for (Long id : ids) {
                DiskItem item = diskItemMapper.findById(id);
                if (item == null || item.getType().equals("FOLDER")) {
                    continue;
                }

                InputStream inputStream = minioService.getObject(item.getBucketName(), item.getObjectKey());
                zos.putNextEntry(new ZipEntry(item.getName()));

                StreamUtils.copy(inputStream, zos);

                zos.closeEntry();
            }

            zos.finish();
            zos.flush();
        } catch (Exception e) {

            // 不要让异常走全局异常处理
            response.reset();
            response.setContentType("application/json");
            response.getWriter().write("{\"code\":500,\"msg\":\"下载失败\"}");
        }
    }
}
