package com.example.netdisk.controller;

import com.example.netdisk.common.Result;
import com.example.netdisk.entity.FileInfo;
import com.example.netdisk.service.FileInfoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

    /**
     * 文件列表
     */
    @GetMapping
    public Result<List<FileInfo>> listFiles() {
        return Result.success(fileInfoService.listFiles());
    }

    /**
     * 下载文件
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable Long id) throws Exception {
        FileInfo fileInfo = fileInfoService.getById(id);

        File file = new File(fileInfo.getStoragePath());
        if(!file.exists()) {
            throw new RuntimeException("文件不存在");
        }

        StreamingResponseBody stream = outputStream -> {
            try (InputStream inputStream = new FileInputStream(file)){
                byte[] buffer = new byte[8192];// 8KB缓冲区
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);// 分块写入
                }
            }
        };

        String fileName;
        try {
            fileName = URLEncoder.encode(fileInfo.getOriginalName(), StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            fileName = fileInfo.getOriginalName();
        }

        //文件流（binary）和 JSON 不能在一个响应体里同时返回，所以这里放在header中
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        return ResponseEntity.ok().headers(headers).body(stream);
    }

}
