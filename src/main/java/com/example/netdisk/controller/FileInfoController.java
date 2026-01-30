package com.example.netdisk.controller;

import com.example.netdisk.common.Result;
import com.example.netdisk.dto.FileInfo.UploadUrlRequest;
import com.example.netdisk.dto.FileInfo.UploadUrlResponse;
import com.example.netdisk.entity.FileInfo;
import com.example.netdisk.service.FileInfoService;
import com.example.netdisk.service.MinioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件管理
 */
@RestController
@RequestMapping("/file")
public class FileInfoController {

    private final FileInfoService fileInfoService;
    private final MinioService minioService;

    public FileInfoController(FileInfoService fileInfoService,
                              MinioService minioService) {
        this.fileInfoService = fileInfoService;
        this.minioService = minioService;
    }

    /**
     * 获取 MinIO 预签名上传 URL（前端直传）
     */
    @PostMapping("/upload-url")
    public Result<UploadUrlResponse> getUploadUrl(
            @RequestBody UploadUrlRequest request) throws Exception {

        // TODO 后续从登录态获取
        Long userId = 1L;

        String objectKey = minioService.buildObjectName(
                userId,
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
     * 前端上传成功后，保存文件信息
     */
    @PostMapping("/save")
    public Result<FileInfo> saveFileInfo(
            @RequestBody FileInfo fileInfo) {

        // TODO 设置用户ID
        fileInfo.setUploaderId(1L);

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

        String url = minioService.getPreviewUrl(
                fileInfo.getObjectKey(),
                fileInfo.getContentType()
        );

        return Result.success(url);
    }
}
