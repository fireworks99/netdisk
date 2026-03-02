package com.example.netdisk.service;

import com.example.netdisk.entity.SysUser;

import java.util.List;

public interface SysUserService {

    void createUser(SysUser user, List<Long> roleIds);

    void deleteUser(Long userId);

    void assignRoles(Long userId, List<Long> roleIds);

    SysUser findById(Long userId);

    List<SysUser> list();

    Boolean existsByUsername(String username);

    SysUser findUserByUsername(String username);

    List<String> findRolesByUserId(Long userId);

    List<String> findPermsByUserId(Long userId);

}
