package com.example.netdisk.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

/**
 * 这个注解的意义：在需要此类实例化的地方，由 Spring 代替我们手动 new 一个对象
 * 相当于手动挡换自动挡
 */
@Component
public class JwtUtil {

//    @Value("${jwt.secret}")
    private String secret;

    //字段初始化的顺序早于依赖注入，这里secret是null，进而抛出异常（方案：用构造函数注入）
//    private final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

    private final SecretKey key;

    // 使用构造函数注入
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 签发令牌（登录成功后的"通行证"）
     * 服务器确认了你的身份（username），现在给你签发一个带有效期的加密令牌，以后你拿着这个令牌来访问，就不用再重复登录了。
     */
    public String generateToken(Long userId, String username, List<String> roles, List<String> perms) {
        return Jwts.builder()// 1. 创建JWT

                // 标准字段
                .subject(username)// 2. 设置主题（Subject）：把用户名存入令牌中，标识这个令牌属于谁
                .issuedAt(new Date()) // 令牌的签发时刻（与过期时间配合使用）
                .expiration(new Date(System.currentTimeMillis() + 7200_000))// 3. 设置过期时间：令牌有效期2小时

                // 自定义字段
                .claim("uid", userId)
                .claim("roles", roles)
                .claim("perms", perms)

                .signWith(key)// 4. 签名加密：用服务器的密钥对令牌进行签名，防止被篡改
                .compact();// 5. 压缩成最终字符串（例如：eyJhbGciOiJIUzI1NiJ9.eyJz...4qd7...）
    }

    /**
     * 解析验证令牌，获取用户名
     * 客户端拿着令牌来访问了，我要验证这个令牌是不是我签发的、有没有被篡改、有没有过期。如果一切正常，取出里面的用户名，确认是谁在访问。
     */
//    public String getUserName(String token) {
//        return Jwts.parser()// 1. 创建解析器
//                .verifyWith(key)// 2. 设置验证所用密钥（告诉解析器用哪个密钥来验证签名）
//                .build()// 3. 构建解析器
//                .parseSignedClaims(token) // 4. 解析并验证JWT
//                .getPayload()// 5. 获取载荷（Claims对象）
//                .getSubject();// 6. 获取主题（用户名）
//    }

    public Claims parseToken(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUserName(String token) {
        return parseToken(token).getSubject();
    }

    public Long getUserId(String token) {
        return parseToken(token).get("uid", Long.class);
    }

    @SuppressWarnings("unchecked")//这个注解：别警告我了，我知道这里有类型安全问题，但我保证没问题
    public List<String> getRoles(String token) {
        return parseToken(token).get("roles", List.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> getPerms(String token) {
        return parseToken(token).get("perms", List.class);
    }

    public Date getExpiration(String token) {
        return parseToken(token).getExpiration();
    }

}

/**
 * 【生成令牌】
 * 用户名"tom"
 *     → .subject("tom")                 (存入sub字段)
 *     → .expiration(明天此时)            (存入exp字段)
 *     → .signWith(密钥)                  (生成签名)
 *     → .compact()                       (打包成: header.payload.signature)
 *     → 返回: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b20iLCJleHAiOjE2Nzg5MDAwMDB9.4qD7X9..."
 *
 * 【解析令牌】
 * 传入: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b20iLCJleHAiOjE2Nzg5MDAwMDB9.4qD7X9..."
 *     → .verifyWith(密钥)                (设置验证密钥)
 *     → .parseSignedClaims(token)        (分割、验签、验期)
 *         ↓ 验证通过
 *     → .getPayload()                    (取出载荷部分: {sub:"tom", exp:1678900000})
 *     → .getSubject()                     (取出sub的值: "tom")
 *     → 返回: "tom"
 */
