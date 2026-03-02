package com.example.netdisk.service.impl;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.entity.SysRole;
import com.example.netdisk.mapper.SysRoleMapper;
import com.example.netdisk.mapper.SysRolePermissionMapper;
import com.example.netdisk.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleMapper roleMapper;
    private final SysRolePermissionMapper rolePermMapper;

    @Override
    public void createRole(SysRole role, List<Long> permIds) {

        roleMapper.insert(role);

        if(permIds != null) {
            for(Long permId: permIds) {
                rolePermMapper.insert(role.getId(), permId);
            }
        }

    }

    @Override
    public void deleteRole(Long roleId) {

        SysRole role = roleMapper.findById(roleId);
        if(role.getRoleCode().equals("ROLE_ADMIN")) {
            throw new RuntimeException("系统角色不可删除");
        }

        rolePermMapper.deleteByRoleId(roleId);
        roleMapper.deleteById(roleId);
    }

    @Override
    public void assignPermissions(Long roleId, List<Long> permIds) {

        rolePermMapper.deleteByRoleId(roleId);

        if(permIds != null) {
            for (Long permId: permIds) {
                rolePermMapper.insert(roleId, permId);
            }
        }
    }

    @Override
    public List<SysRole> list() {
        return roleMapper.findAll();
    }

    @Override
    public PageResult<SysRole> page(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;

        List<SysRole> list = roleMapper.pageQuery(offset, pageSize);
        long total = roleMapper.countQuery();

        return new PageResult<>(total, list);
    }
}
