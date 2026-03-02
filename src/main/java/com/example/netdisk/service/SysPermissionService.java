package com.example.netdisk.service;

import com.example.netdisk.entity.SysPermission;

import java.util.List;

public interface SysPermissionService {

    void create(SysPermission permission);

    void delete(Long id);

    List<SysPermission> list();

}
