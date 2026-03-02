package com.example.netdisk.controller;

import com.example.netdisk.common.Result;
import com.example.netdisk.dto.User.UpDTO;
import com.example.netdisk.entity.SysUser;
import com.example.netdisk.exception.BusinessException;
import com.example.netdisk.security.jwt.JwtUtil;
import com.example.netdisk.service.SysUserService;
import com.example.netdisk.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 身份认证
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SysUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody UpDTO dto) {

        if(userService.existsByUsername(dto.getUsername())) {
            throw new BusinessException(409, "用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        // 默认给普通用户角色
        userService.createUser(user, Collections.singletonList(2L));

        return Result.successMsg("注册成功");
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody UpDTO dto) {

        // 触发 Spring Security 的认证流程
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );


        SysUser user = userService.findUserByUsername(dto.getUsername());

        LoginVO vo = new LoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setStatus(user.getStatus());

        List<String> roles = userService.findRolesByUserId(user.getId());
        List<String> perms = userService.findPermsByUserId(user.getId());

        String token = jwtUtil.generateToken(user.getId(), dto.getUsername(), roles, perms);
        vo.setToken(token);

        Date expiration = jwtUtil.getExpiration(token);
        vo.setExpiration(expiration);

        return Result.success(vo);
    }
}
