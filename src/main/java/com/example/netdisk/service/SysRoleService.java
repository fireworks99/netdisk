package com.example.netdisk.service;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.entity.SysRole;

import java.util.List;

public interface SysRoleService {

    void createRole(SysRole role, List<Long> permIds);

    void deleteRole(Long roleId);

    void assignPermissions(Long roleId, List<Long> permIds);

    List<SysRole> list();

    PageResult<SysRole> page(int pageNum, int pageSize);

}
