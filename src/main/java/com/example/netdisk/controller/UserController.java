package com.example.netdisk.controller;

import com.example.netdisk.entity.User;
import com.example.netdisk.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @RestController = @Controller + @ResponseBody
 */
@RestController
public class UserController {

    private final UserMapper userMapper;

    public  UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/test/users")
    public List<User> listUsers() {
        return userMapper.findAll();
    }

}
