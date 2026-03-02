package com.example.netdisk.security.filter;

import com.example.netdisk.security.jwt.JwtUtil;
import com.example.netdisk.vo.LoginUserVO;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * OncePerRequestFilter：确保在一个外部请求的处理过程中，本过滤器只会被执行一次。
 */

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if(header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            Long userId = jwtUtil.getUserId(token);
            String username = jwtUtil.getUserName(token);
            List<String> roles = jwtUtil.getRoles(token);
            List<String> perms = jwtUtil.getPerms(token);

            LoginUserVO loginUser = new LoginUserVO(userId, username, roles, perms);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(loginUser, null, null);

            // 将认证信息绑定到当前线程的上下文中
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 放行请求，继续执行后续的过滤器或目标 Controller
        filterChain.doFilter(request, response);
    }
}

/**
 * 登录：
 *
 * 1. 用户 POST /auth/login (用户名+密码)
 *     ↓
 * 2. 请求到达，JwtFilter检查Authorization头 → 没有令牌
 *     ↓
 * 3. 请求继续传递到登录Controller
 *     ↓
 * 4. 登录Controller验证用户名密码（用PasswordEncoder比对）
 *     ↓
 * 5. 验证成功 → 调用JwtUtil生成令牌 → 返回给客户端
 */

/**
 * 登录后访问受保护的资源：
 *
 * 1. 用户 GET /api/user/info (携带 Bearer token)
 *     ↓
 * 2. 请求到达JwtFilter
 *     ↓
 * 3. 解析token → 获取用户名 → 加载用户信息 → 存入SecurityContext
 *     ↓
 * 4. 请求继续传递
 *     ↓
 * 5. 后续过滤器检查SecurityContext → 已有认证信息 → 允许访问
 *     ↓
 * 6. 到达目标Controller
 */