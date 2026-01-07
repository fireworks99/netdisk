package com.example.netdisk.service;

import com.example.netdisk.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileInfoService {

    FileInfo upload(MultipartFile multipartFile);

    List<FileInfo> listFiles();

    FileInfo getById(Long id);

}
