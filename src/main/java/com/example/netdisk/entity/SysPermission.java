package com.example.netdisk.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysPermission {

    private Long id;
    private String permissionName;
    private String permissionCode;
    private String type;
    private Long parentId;
    private String path;
    private String method;
    private LocalDateTime createTime;

}
