package com.example.netdisk.mapper;

import com.example.netdisk.entity.SysPermission;

import java.util.List;

public interface SysPermissionMapper {

    List<SysPermission> findAll();

    int insert(SysPermission permission);

    int deleteById(Long id);

}
