package com.example.netdisk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

/**
 * 报错：Parameter 0 of constructor in com.example.netdisk.controller.UserController
 * required a bean of type 'com.example.netdisk.mapper.UserMapper'
 * that could not be found.
 *
 * 翻译：未在容器中找到UserMapper的Bean
 *
 * 解决：@MapperScan("com.example.netdisk.mapper")
 *
 * 启动时扫描 com.example.netdisk.mapper 包下所有 Mapper 接口
 * 并把它们注册成 Spring Bean
 * 就可以在Service里直接注入(@Autowired)使用
 */
@MapperScan("com.example.netdisk.mapper")
public class NetdiskApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetdiskApplication.class, args);
    }

}
