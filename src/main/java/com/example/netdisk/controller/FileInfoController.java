package com.example.netdisk.controller;

import com.example.netdisk.common.Result;
import com.example.netdisk.dto.FileInfo.UploadUrlRequest;
import com.example.netdisk.dto.FileInfo.UploadUrlResponse;
import com.example.netdisk.entity.FileInfo;
import com.example.netdisk.security.utils.SecurityUtils;
import com.example.netdisk.service.FileInfoService;
import com.example.netdisk.service.impl.MinioServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件管理（弃用）
 */
@RestController
@RequestMapping("/file")
public class FileInfoController {

    private final FileInfoService fileInfoService;
    private final MinioServiceImpl minioServiceImpl;

    public FileInfoController(FileInfoService fileInfoService,
                              MinioServiceImpl minioServiceImpl) {
        this.fileInfoService = fileInfoService;
        this.minioServiceImpl = minioServiceImpl;
    }

    /**
     * 获取 MinIO 预签名上传 URL（前端直传）
     */
    @PostMapping("/upload-url")
    public Result<UploadUrlResponse> getUploadUrl(
            @RequestBody UploadUrlRequest request) throws Exception {

        Long userId = SecurityUtils.getUserId();;

        String objectKey = minioServiceImpl.buildObjectName(
                userId,
                request.getOriginalName()
        );

        String uploadUrl = minioServiceImpl.getUploadUrl(
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
    @PostMapping("/save")
    public Result<FileInfo> saveFileInfo(
            @RequestBody FileInfo fileInfo) {

        fileInfo.setUploaderId(SecurityUtils.getUserId());

        return Result.success(
                fileInfoService.saveFileInfo(fileInfo)
        );
    }

    /**
     * 文件列表
     */
    @GetMapping
    public Result<List<FileInfo>> listFiles() {
        return Result.success(fileInfoService.listFiles());
    }

    /**
     * 获取文件访问 URL（下载 / 预览通用）
     */
    @GetMapping("/{id}/url")
    public Result<String> getFileUrl(@PathVariable Long id) {

        FileInfo fileInfo = fileInfoService.getById(id);

        String url = minioServiceImpl.getPreviewUrl(
                fileInfo.getObjectKey(),
                fileInfo.getContentType()
        );

        return Result.success(url);
    }
}
