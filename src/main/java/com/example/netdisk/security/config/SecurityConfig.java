package com.example.netdisk.security.config;

import com.example.netdisk.security.filter.JwtFilter;
import com.example.netdisk.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 这个 SecurityConfig 类是 Spring Security 的核心配置类
 * 它定义了整个应用的安全策略：哪些请求需要认证、使用什么认证方式、密码如何加密等
 */

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // 暴露 AuthenticationManager 作为 Bean（登录用到了）
    // Spring Security的核心认证接口，处理认证请求（如登录验证）
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();//拿到默认的 认证管理器
    }

    /**
     *  Spring Security 的密码校验逻辑在 DaoAuthenticationProvider
     *  Spring Boot 的自动配置发现缺少 AuthenticationProvider,
     *  会自动创建一个 DaoAuthenticationProvider，然后从容器中找到
     *  UserDetailsService 和 PasswordEncoder 并注入，
     *  流程大概如下
     */
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider(
//            CustomUserDetailsService userDetailsService,
//            PasswordEncoder passwordEncoder) {
//
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//
//        provider.setUserDetailsService(userDetailsService);     // 告诉provider如何查用户
//        provider.setPasswordEncoder(passwordEncoder);           // 告诉provider如何比对密码
//
//        return provider;
//    }

    /**
     * Bean注解：将当前方法的返回值注册为一个 Bean（一个由 Spring 管理的对象）
     * JWT认证方式，必须自定义SecurityFilterChain来跳过默认的 (Session认证)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()// 关闭CSRF保护：JWT认证通常用于REST API，且JWT存在浏览器LocalStorage中，CSRF防护与无状态API不兼容
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 设置无状态会话

                .and()//返回 HttpSecurity
                .authorizeRequests()// 开始配置URL授权（新版可能用 authorizeHttpRequests）
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()// 预检请求放行
                .antMatchers("/auth/**").permitAll()// 登录接口所有人都可访问
                .anyRequest().authenticated()// 其他所有请求都需要认证

                .and()//返回 HttpSecurity
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);// 将自定义的 JwtFilter 插入到过滤器链中
                // 为什么放在它之前：因为我们希望先验证JWT，如果JWT有效就直接认证成功，这种情况下免了一次密码登录
                // [匿名过滤器] → [JwtFilter] → [UsernamePasswordAuthenticationFilter] → [其他过滤器] → [目标Controller]

        return http.build();
    }

    /**
     * 提供密码加密和验证功能
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

/**
 * 更详细的授权配置
 * .authorizeRequests()
 *     .antMatchers("/auth/login", "/auth/register", "/public/**").permitAll()  // 公共接口
 *     .antMatchers("/admin/**").hasRole("ADMIN")  // 需要ADMIN角色
 *     .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")  // USER或ADMIN角色
 *     .antMatchers("/api/**").authenticated()  // 需要认证
 *     .anyRequest().denyAll()  // 其他所有请求都拒绝
 */

/**
 * 默认过滤器链包含（简化版）：
 * ├── CsrfFilter                 // CSRF防护 （❌️被禁用了）
 * ├── LogoutFilter               // 注销处理
 * ├── JwtFilter                  // 自定义JWT验证
 * ├── UsernamePasswordAuthenticationFilter  // 表单登录
 * ├── DefaultLoginPageGeneratingFilter      // 生成登录页
 * ├── DefaultLogoutPageGeneratingFilter     // 生成注销页
 * ├── BasicAuthenticationFilter  // HTTP Basic认证
 * ├── RequestCacheFilter         // 请求缓存
 * ├── SecurityContextHolderFilter // 安全上下文
 * ├── AnonymousAuthenticationFilter // 匿名认证
 * ├── SessionManagementFilter    // 会话管理
 * ├── ExceptionTranslationFilter  // 异常转换
 * └── FilterSecurityInterceptor  // 权限校验
 *
 * 请求到达
 *     ↓
 * 1. JwtFilter先执行
 *    ↓ 如果有有效的JWT
 *    ↓ 创建UsernamePasswordAuthenticationToken
 *    ↓ 设置到SecurityContext
 *     ↓
 * 2. UsernamePasswordAuthenticationFilter执行
 *    ↓ 发现SecurityContext中已经有认证信息
 *    ↓ 跳过用户名密码认证
 *     ↓
 * 3. 请求继续
 */

