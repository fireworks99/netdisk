package com.example.netdisk.mapper;

import com.example.netdisk.entity.FileInfo;

import java.util.List;

public interface FileInfoMapper {
    int insert(FileInfo fileInfo);

    List<FileInfo> findAll();

    FileInfo findById(Long id);
}
