package com.example.netdisk.controller;

import com.example.netdisk.common.Result;
import com.example.netdisk.entity.User;
import com.example.netdisk.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理
 *
 * @RestController = @Controller + @ResponseBody
 *
 * @Controller: 标记一个类作为 Spring MVC 控制器
 * 用于传统的 Web 应用，返回视图（HTML 页面）
 * 方法通常返回字符串（视图名称）或 ModelAndView 对象
 * 需要配合视图解析器（如 Thymeleaf、JSP）使用
 *
 * @ResponseBody: 将方法的返回值直接写入 HTTP 响应体中
 * 通常返回 JSON/XML 数据，而不是视图
 * 配合 @Controller 使用，将方法标记为返回数据而非视图
 * Spring 会自动使用 HttpMessageConverter 进行序列化
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 依赖声明：
     * UserController 需要用到 UserMapper
     * 不是手动new一个，而是由Spring注入
     */
//    private final UserMapper userMapper;

    private final UserService userService;

    /**
     * 构造器注入（而非@Autowired）
     * Spring启动时因为@SpringBootApplication而扫描到这里的@RestController
     * 于是Spring要实例化UserController这个Bean，并将其放入容器中管理
     * 实例化时发现它只有一个构造方法，构造方法需要UserMapper
     * 于是从容器中找类型是 UserMapper 的 Bean
     * 多亏了@MapperScan注解实例化了UserMapper这个Bean
     * 于是将它传到这里
     *
     * @param userMapper
     */
    //    public  UserController(UserMapper userMapper) {
    //        this.userMapper = userMapper;
    //    }

    /**
     * Controller：构造器注入 Service
     * Service：构造器注入 Mapper
     *
     * @param userService
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询所有用户
     *
     * 因为有@RestController
     * Spring把 List<User> 转成 JSON
     * 并设置Content-Type: application/json
     *
     * @GetMapping("/test/users")等价于：
     * @RequestMapping(value="/test/users",method=RequestMethod.GET)
     */
    @GetMapping
    public Result<List<User>> listUsers() {
//        return userMapper.findAll();
        return Result.success(userService.listUsers());
    }

}
