package com.example.netdisk.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 */
@RestController
public class TestController {

    /**
     * 测试连接
     * @return
     */
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
