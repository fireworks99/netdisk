package com.example.netdisk.service;

import com.example.netdisk.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileInfoService {

    FileInfo upload(MultipartFile multipartFile);

}
