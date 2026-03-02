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
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     *  Spring Security 的密码校验逻辑在 DaoAuthenticationProvider
     *  而它依赖 userDetailsService ，删了它会造成 StackOverflow
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    /**
     * Bean注解：将当前方法的返回值注册为一个 Bean（一个由 Spring 管理的对象）
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()// 关闭CSRF保护：JWT认证通常用于REST API，且JWT存在浏览器LocalStorage中，CSRF防护与无状态API不兼容
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 设置无状态会话
                .and()
                .authorizeRequests()// 开始配置URL授权（新版可能用 authorizeHttpRequests）
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()// 预检请求放行
                .antMatchers("/auth/**").permitAll()// 登录接口所有人都可访问
                .anyRequest().authenticated()// 其他所有请求都需要认证
                .and()
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