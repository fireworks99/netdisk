package com.example.netdisk.mapper;

import com.example.netdisk.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

public interface SysUserRoleMapper {

    int insert(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int deleteByUserId(Long userId);

}
