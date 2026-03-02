package com.example.netdisk.controller;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.common.Result;
import com.example.netdisk.dto.Role.RoleCreateDTO;
import com.example.netdisk.entity.SysRole;
import com.example.netdisk.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    /**
     * 创建角色
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void create(@RequestBody RoleCreateDTO dto) {
        roleService.createRole(dto.getRole(), dto.getPermIds());
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        roleService.deleteRole(id);
    }

    /**
     * 给角色授权
     */
    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public void assignPermission(@PathVariable Long id, @RequestBody List<Long> permIds) {
        roleService.assignPermissions(id, permIds);
    }

    /**
     * 所有角色
     */
    @GetMapping
    public List<SysRole> list() {
        return roleService.list();
    }

    /**
     * 分页查询角色
     */
    @GetMapping("/page")
    public Result<PageResult<SysRole>> page(@RequestParam int pageNum, @RequestParam int pageSize) {
        return Result.success(roleService.page(pageNum, pageSize));
    }

}
