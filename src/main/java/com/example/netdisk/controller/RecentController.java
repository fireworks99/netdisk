package com.example.netdisk.controller;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.common.Result;
import com.example.netdisk.entity.DiskItem;
import com.example.netdisk.service.RecentService;
import com.example.netdisk.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 最近访问
 */
@RestController
@RequestMapping("/recent")
@RequiredArgsConstructor
public class RecentController {

    private final RecentService recentService;
    private final SysUserService userService;

    private Long getUserId() {
        return userService.getUserId();
    }

    /**
     * 分页查询最近访问
     */
    @GetMapping("/page")
    public Result<PageResult<DiskItem>> page(@RequestParam int pageNum,
                                             @RequestParam int pageSize) {

        return Result.success(
                recentService.page(getUserId(), pageNum, pageSize)
        );
    }

}
