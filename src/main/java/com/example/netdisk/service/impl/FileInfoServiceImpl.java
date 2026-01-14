package com.example.netdisk.service.impl;

import com.example.netdisk.config.FileUploadProperties;
import com.example.netdisk.entity.FileInfo;
import com.example.netdisk.exception.BusinessException;
import com.example.netdisk.mapper.FileInfoMapper;
import com.example.netdisk.service.FileInfoService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class FileInfoServiceImpl implements FileInfoService {

    private final FileUploadProperties fileUploadProperties;

    private final FileInfoMapper fileInfoMapper;

    public FileInfoServiceImpl(FileUploadProperties fileUploadProperties, FileInfoMapper fileInfoMapper) {
        this.fileUploadProperties = fileUploadProperties;
        this.fileInfoMapper = fileInfoMapper;
    }

    @Override
    public FileInfo upload(MultipartFile file) {

        if(file.isEmpty()) {
//            throw new RuntimeException("文件不能为空");
            throw new BusinessException(400, "文件不能为空");
        }

        String originalName = file.getOriginalFilename();

        String suffix = "";
        if (originalName != null && originalName.contains(".")) {
            suffix = originalName.substring(originalName.lastIndexOf("."));
        }
        String storageName = UUID.randomUUID().toString() + suffix;

        String basePath = fileUploadProperties.getBasePath();
        String storagePath = basePath + "/" + storageName;

        File dest = new File(storagePath);
        dest.getParentFile().mkdir();

        try {
            file.transferTo(dest);
        } catch (IOException e) {
//            throw new RuntimeException("文件保存失败", e);
            throw new BusinessException(500, "文件保存失败");
        }

        FileInfo fileInfo = new FileInfo();
        fileInfo.setOriginalName(originalName);
        fileInfo.setStorageName(storageName);
        fileInfo.setStoragePath(storagePath);
        fileInfo.setFileSize(file.getSize());
        fileInfo.setContentType(file.getContentType());

        fileInfoMapper.insert(fileInfo);

        return fileInfo;
    }

    @Override
    public List<FileInfo> listFiles() {
        return fileInfoMapper.findAll();
    }

    @Override
    public FileInfo getById(Long id) {
        FileInfo fileInfo = fileInfoMapper.findById(id);
        if(fileInfo == null) {
//            throw new RuntimeException("文件不存在");
            throw new BusinessException(404, "文件不存在");
        }
        return fileInfo;
    }
}
