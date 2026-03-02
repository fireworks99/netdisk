package com.example.netdisk.service.impl;

import com.example.netdisk.entity.SysPermission;
import com.example.netdisk.mapper.SysPermissionMapper;
import com.example.netdisk.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SysPermissionServiceImpl implements SysPermissionService {

    private final SysPermissionMapper permissionMapper;

    @Override
    public void create(SysPermission permission) {
        permissionMapper.insert(permission);
    }

    @Override
    public void delete(Long id) {
        permissionMapper.deleteById(id);
    }

    @Override
    public List<SysPermission> list() {
        return permissionMapper.findAll();
    }
}
