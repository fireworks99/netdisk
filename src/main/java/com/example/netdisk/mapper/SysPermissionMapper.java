package com.example.netdisk.mapper;

import com.example.netdisk.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPermissionMapper {

    List<SysPermission> findAll();

    int insert(SysPermission permission);

    int deleteById(Long id);

    List<SysPermission> pageQuery(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    Long countQuery();

}
