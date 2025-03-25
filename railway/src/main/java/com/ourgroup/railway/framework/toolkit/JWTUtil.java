package com.ourgroup.railway.framework.toolkit;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import java.util.Date;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;

public class JWTUtil {

    // 秘钥，通常应该保存在配置文件中
    private static final String SECRET_KEY = "your-secret-key";

    // 过期时间（以毫秒为单位）
    private static final long EXPIRATION_TIME = 3600000L; // 1小时

    // 生成JWT令牌
    public static String generateAccessToken(String username, String realName) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
         // 生成 SecretKey
        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        // 生成JWT
        return Jwts.builder()
                .subject(username) // 用户名作为JWT的主题
                .claim("realName", realName) // 你可以添加其他信息到JWT的负载中
                .issuedAt(now) // 设置签发时间
                .expiration(expiryDate) // 设置过期时间
                .signWith(secretKey) // 使用HS512算法进行签名
                .compact();
    }

    // 解析JWT，获取Claims（其中包含了payload部分的数据）
    public static Claims parseToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    // 获取JWT中的用户名
    public static String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject(); // 返回JWT中的用户名
    }

    // 验证JWT是否有效
    public static boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }

    // 检查JWT是否有效
    public static boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
