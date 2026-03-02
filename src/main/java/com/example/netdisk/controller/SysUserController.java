package com.example.netdisk.controller;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.common.Result;
import com.example.netdisk.dto.User.UserCreateDTO;
import com.example.netdisk.entity.SysUser;
import com.example.netdisk.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    /**
     * 创建用户
     */
    @PostMapping
    @PreAuthorize("hasAuthority('user:add')")
    public void create(@RequestBody UserCreateDTO dto) {
        userService.createUser(dto.getUser(), dto.getRoleIds());
    }

    /**
     * 删除用户
     *
     * 检查当前请求的发送者是否有 user:delete 权限，
     * 是从 SecurityContext 中获取的
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /**
     * 给用户分配角色
     */
    @PutMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('user:add')")
    public void assignRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        userService.assignRoles(id, roleIds);
    }

    /**
     * 查询所有用户
     */
    @GetMapping
    public List<SysUser> list() {
        return userService.list();
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    public Result<PageResult<SysUser>> page(@RequestParam int pageNum, @RequestParam int pageSize) {
        return Result.success(userService.page(pageNum, pageSize));
    }
}
