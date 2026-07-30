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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
        // 如果认证失败，这行代码会抛出异常，后面的代码不会执行
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        List<String> roles = new ArrayList<>();
        List<String> perms = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            String s = authority.getAuthority();
            if (s != null) {
                if (s.startsWith("ROLE_")) {
                    roles.add(s);
                } else {
                    perms.add(s);
                }
            }
        }

        SysUser user = userService.findUserByUsername(dto.getUsername());

        // 上面从内存里取，代替这里从数据库取
//        List<String> roles = userService.findRolesByUserId(user.getId());
//        List<String> perms = userService.findPermsByUserId(user.getId());

        LoginVO vo = new LoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setStatus(user.getStatus());
        vo.setRoles(roles);
        vo.setPerms(perms);

        String token = jwtUtil.generateToken(dto.getUsername());
        vo.setToken(token);

        Date expiration = jwtUtil.getExpiration(token);
        vo.setExpiration(expiration);

        return Result.success(vo);
    }
}
