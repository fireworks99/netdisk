package com.example.netdisk.mapper;

import com.example.netdisk.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;

public interface SysRolePermissionMapper {

    int insert(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    int deleteByRoleId(Long roleId);

}
