package com.example.netdisk.service;

import com.example.netdisk.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileInfoService {

    String generateUploadUrl(String originalName, String contentType);

    FileInfo saveFileInfo(FileInfo fileInfo);

    List<FileInfo> listFiles();

    FileInfo getById(Long id);

}
