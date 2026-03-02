package com.example.netdisk.controller;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.common.Result;
import com.example.netdisk.entity.SysPermission;
import com.example.netdisk.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理
 */
@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class SysPermissionController {

    private final SysPermissionService permissionService;

    /**
     * 创建权限
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void create(@RequestBody SysPermission permission) {
        permissionService.create(permission);
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        permissionService.delete(id);
    }

    /**
     * 查询所有权限
     */
    @GetMapping
    public List<SysPermission> list() {
        return permissionService.list();
    }

    /**
     * 分页查询权限
     */
    @GetMapping("/page")
    public Result<PageResult<SysPermission>> page(@RequestParam int pageNum, @RequestParam int pageSize) {
        return Result.success(permissionService.page(pageNum, pageSize));
    }

}
