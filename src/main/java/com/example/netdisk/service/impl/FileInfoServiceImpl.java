package com.example.netdisk.service.impl;

import com.example.netdisk.entity.FileInfo;
import com.example.netdisk.exception.BusinessException;
import com.example.netdisk.mapper.FileInfoMapper;
import com.example.netdisk.service.FileInfoService;
import io.minio.MinioClient;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.http.Method;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileInfoServiceImpl implements FileInfoService {

    private final FileInfoMapper fileInfoMapper;
    private final MinioClient minioClient;

    private static final String BUCKET_NAME = "netdisk";

    public FileInfoServiceImpl(FileInfoMapper fileInfoMapper,
                               MinioClient minioClient) {
        this.fileInfoMapper = fileInfoMapper;
        this.minioClient = minioClient;
    }

    /**
     * 1️⃣ 生成前端直传所需的 objectKey + 预签名 URL
     */
    @Override
    public String generateUploadUrl(String originalName, String contentType) {

        if (originalName == null || originalName.isEmpty()) {
            throw new BusinessException(400, "文件名不能为空");
        }

        String fileExt = "";
        if (originalName.contains(".")) {
            fileExt = originalName.substring(originalName.lastIndexOf("."));
        }

        // 对象存储里的唯一 key（不是文件名）
        String objectKey = "upload/"
                + LocalDateTime.now().toLocalDate()
                + "/"
                + UUID.randomUUID()
                + fileExt;

        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectKey)
                            .method(Method.PUT)
                            .expiry(10 * 60) // 10 分钟
                            .build()
            );
        } catch (Exception e) {
            throw new BusinessException(500, "生成上传地址失败");
        }
    }

    /**
     * 2️⃣ 前端上传完成后，回调此接口保存文件信息
     */
    @Override
    public FileInfo saveFileInfo(FileInfo fileInfo) {

        if (fileInfo.getObjectKey() == null) {
            throw new BusinessException(400, "objectKey 不能为空");
        }

        fileInfo.setBucketName(BUCKET_NAME);
        fileInfo.setCreateTime(LocalDateTime.now());

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
        if (fileInfo == null) {
            throw new BusinessException(404, "文件不存在");
        }
        return fileInfo;
    }
}
