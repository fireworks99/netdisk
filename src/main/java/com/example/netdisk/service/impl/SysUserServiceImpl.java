package com.example.netdisk.service.impl;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.entity.SysUser;
import com.example.netdisk.mapper.SysUserMapper;
import com.example.netdisk.mapper.SysUserRoleMapper;
import com.example.netdisk.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;//体现出@Bean的作用了

    @Override
    public void createUser(SysUser user, List<Long> roleIds) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(true);

        userMapper.insert(user);

        if(roleIds != null && !roleIds.isEmpty()) {
            for(Long roleId: roleIds) {
                userRoleMapper.insert(user.getId(), roleId);
            }
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRoleMapper.deleteByUserId(userId);
        userMapper.deleteById(userId);
    }

    @Override
    public void assignRoles(Long userId, List<Long> roleIds) {
        userRoleMapper.deleteByUserId(userId);
        if(roleIds != null) {
            for(Long roleId: roleIds) {
                userRoleMapper.insert(userId, roleId);
            }
        }
    }

    @Override
    public SysUser findById(Long userId) {
        return userMapper.findById(userId);
    }

    @Override
    public List<SysUser> list() {
        return userMapper.findAll();
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userMapper.findUserByUsername(username) != null;
    }

    @Override
    public SysUser findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    @Override
    public List<String> findRolesByUserId(Long userId) {
        return userMapper.findRolesByUserId(userId);
    }

    @Override
    public List<String> findPermsByUserId(Long userId) {
        return userMapper.findPermsByUserId(userId);
    }

    @Override
    public PageResult<SysUser> page(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;

        List<SysUser> list = userMapper.pageQuery(offset, pageSize);
        long total = userMapper.countQuery();

        return new PageResult<>(total, list);
    }
}
