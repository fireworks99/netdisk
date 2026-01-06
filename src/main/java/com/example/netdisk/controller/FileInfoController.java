package com.example.netdisk.controller;

import com.example.netdisk.common.Result;
import com.example.netdisk.entity.FileInfo;
import com.example.netdisk.service.FileInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理
 */
@RestController
@RequestMapping("/file")
public class FileInfoController {

    private final FileInfoService fileInfoService;

    public FileInfoController(FileInfoService fileInfoService) {
        this.fileInfoService = fileInfoService;
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<FileInfo> upload(@RequestParam("file")MultipartFile file) {
        FileInfo fileInfo = fileInfoService.upload(file);
        return Result.success(fileInfo);
    }

}
