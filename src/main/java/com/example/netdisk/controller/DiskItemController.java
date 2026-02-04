package com.example.netdisk.controller;

import com.example.netdisk.common.Result;
import com.example.netdisk.dto.FileInfo.UploadUrlRequest;
import com.example.netdisk.dto.FileInfo.UploadUrlResponse;
import com.example.netdisk.entity.DiskItem;
import com.example.netdisk.exception.BusinessException;
import com.example.netdisk.service.MinioService;
import com.example.netdisk.service.impl.DiskItemServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网盘
 */
@RestController
@RequestMapping("/disk")
public class DiskItemController {

    private final DiskItemServiceImpl diskItemService;
    private final MinioService minioService;

    public DiskItemController(DiskItemServiceImpl diskItemService, MinioService minioService) {
        this.diskItemService = diskItemService;
        this.minioService = minioService;
    }

    // 获取当前登录用户
    private Long getUserId() {
        // Todo 从登录态获取
        return 1L;
    }

    /**
     * 查询文件夹与文件
     */
    @GetMapping("/items")
    public Result<List<DiskItem>> list(@RequestParam(required = false) Long parentId) {
        return Result.success(diskItemService.list(getUserId(), parentId));
    }

    /**
     * 创建文件夹
     */
    @PostMapping("/folder")
    public Result<DiskItem> createFolder(@RequestBody DiskItem request) {
        return Result.success(diskItemService.createFolder(getUserId(), request.getName(), request.getParentId()));
    }

    /**
     * 获取 MinIO 预签名上传 URL（前端直传）
     */
    @PostMapping("/upload-url")
    public Result<UploadUrlResponse> getUploadUrl(
            @RequestBody UploadUrlRequest request) throws Exception {

        String objectKey = minioService.buildObjectName(
                getUserId(),
                request.getOriginalName()
        );

        String uploadUrl = minioService.getUploadUrl(
                objectKey,
                request.getContentType()
        );

        return Result.success(
                new UploadUrlResponse(uploadUrl, objectKey)
        );
    }

    /**
     * 保存文件信息
     */
    @PostMapping("/file")
    public Result<DiskItem> saveFile(@RequestBody DiskItem item) {
        item.setOwnerId(getUserId());
        return Result.success(diskItemService.saveFile(item));
    }

    /**
     * 删除文件（夹）
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        diskItemService.delete(getUserId(), id);
        return Result.success(null);
    }

    /**
     * 预览文件
     */
    @GetMapping("/{id}/url")
    public Result<String> preview(@PathVariable Long id) {

        DiskItem item = diskItemService.getById(getUserId(), id);
        if(!"FILE".equals(item.getType())) {
            throw new BusinessException(400, "文件夹不支持预览");
        }

        return Result.success(minioService.getPreviewUrl(item.getObjectKey(), item.getContentType()));
    }
}
