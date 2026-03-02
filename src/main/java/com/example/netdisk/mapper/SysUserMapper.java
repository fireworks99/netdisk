package com.example.netdisk.mapper;

import com.example.netdisk.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {

    SysUser findUserByUsername(String username);

    List<String> findRolesByUserId(Long userId);

    List<String> findPermsByUserId(Long userId);

    int insert(SysUser user);

    int deleteById(Long userId);

    SysUser findById(Long userId);

    List<SysUser> findAll();

    List<SysUser> pageQuery(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    Long countQuery();

}
