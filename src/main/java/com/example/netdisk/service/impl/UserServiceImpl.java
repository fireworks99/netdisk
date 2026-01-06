package com.example.netdisk.service.impl;

import com.example.netdisk.entity.User;
import com.example.netdisk.mapper.UserMapper;
import com.example.netdisk.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    /**
     * Controller：构造器注入 Service
     * Service：构造器注入 Mapper
     *
     * @param userMapper
     */
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> listUsers() {
        // 现在只是转发
        // 以后业务逻辑都写在这里
        return userMapper.findAll();
    }
}
