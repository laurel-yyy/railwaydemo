package com.ourgroup.railway.framework.toolkit;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import java.util.Date;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JWTUtil {

    private static final long EXPIRATION = 86400L;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ISS = "index12306";
    public static final String SECRET = "SecretKey039245678901232039487623456783092349288901402967890140939827";

    // 生成JWT令牌
    public static String generateAccessToken(String username, String realName) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION);
         // 生成 SecretKey
        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

        // 生成JWT
        return Jwts.builder()
                .setSubject(username) // 用户名作为JWT的主题
                .claim("realName", realName) // 你可以添加其他信息到JWT的负载中
                .setIssuedAt(now) // 设置签发时间
                .setExpiration(expiryDate) // 设置过期时间
                .signWith(SignatureAlgorithm.HS512, secretKey.getEncoded()) // 使用HS512算法进行签名
                .compact();
    }

    // 解析JWT，获取Claims（其中包含了payload部分的数据）
    public static Claims parseToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
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
