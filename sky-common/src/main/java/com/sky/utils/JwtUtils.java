package com.sky.utils;

import com.sky.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类，提供生成和解析 JWT 的方法
 */
@Component
public class JwtUtils {
    @Autowired
    private JwtProperties jwtProperties;

    private static String SECRET_KEY;
    private static Long EXPIRE_TIME;

    @PostConstruct
    public void init() {
        if (jwtProperties != null) {
            SECRET_KEY = jwtProperties.getSecretKey();
            EXPIRE_TIME = jwtProperties.getExpireTime();
        }
    }

    /**
     * 生成JWT
     *
     * @param claims 要加入token的声明集合
     * @return 生成的JWT字符串
     */
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, SECRET_KEY).addClaims(claims).setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME)).compact();
    }

    /**
     * 解析JWT
     *
     * @param token 待解析的JWT字符串
     * @return 解析后的Claims对象
     */
    public static Claims parseJwt(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
}