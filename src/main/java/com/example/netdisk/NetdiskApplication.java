package com.example.netdisk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @SpringBootApplication = @SpringBootConfiguration + @EnableAutoConfiguration + @ComponentScan
 * @SpringBootConfiguration
 * @EnableAutoConfiguration
 * @ComponentScan： 没写 basePackages，意味着默认扫描“当前类所在包 + 所有子包”
 *
 * Spring启动过程：
 * 1. 找到启动类 NetdiskApplication
 * 2. 读取 @SpringBootApplication
 * 3. 发现其中包含 @ComponentScan
 * 4. 扫描包：com.example.netdisk 及其子包
 * 5. 扫描到：
 *    - @RestController
 *    - @Controller
 *    - @Service
 *    - @Component
 *    - @Configuration
 * 6. 把这些类注册为 Bean
 */
@SpringBootApplication
@EnableScheduling
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
