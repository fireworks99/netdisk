package com.example.netdisk.mapper;

import com.example.netdisk.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {

    List<SysRole> findAll();

    int insert(SysRole role);

    int deleteById(Long id);

    SysRole findById(Long id);

    List<SysRole> pageQuery(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    Long countQuery();

}
