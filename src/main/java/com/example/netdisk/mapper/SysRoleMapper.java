package com.example.netdisk.mapper;

import com.example.netdisk.entity.SysRole;

import java.util.List;

public interface SysRoleMapper {

    List<SysRole> findAll();

    int insert(SysRole role);

    int deleteById(Long id);

    SysRole findById(Long id);

}
