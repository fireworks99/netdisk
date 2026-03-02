package com.example.netdisk.controller;

import com.example.netdisk.common.PageResult;
import com.example.netdisk.common.Result;
import com.example.netdisk.entity.DiskItem;
import com.example.netdisk.security.utils.SecurityUtils;
import com.example.netdisk.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 我的收藏
 */
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 获取当前登录用户
    private Long getUserId() {
        return SecurityUtils.getUserId();
    }

    /**
     * 收藏
     */
    @PostMapping("/{diskItemId}")
    public Result<Void> add(@PathVariable Long diskItemId) {

        if(favoriteService.exists(getUserId(), diskItemId)) {
            return Result.successMsg("文件早已被收藏");
        }

        favoriteService.add(getUserId(), diskItemId);
        return Result.successMsg("收藏成功");
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/{diskItemId}")
    public Result<Void> remove(@PathVariable Long diskItemId) {

        favoriteService.remove(getUserId(), diskItemId);
        return Result.successMsg(null);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<PageResult<DiskItem>> page(@RequestParam int pageNum,
                                             @RequestParam int pageSize) {

        return Result.success(favoriteService.page(getUserId(), pageNum, pageSize));
    }

}
