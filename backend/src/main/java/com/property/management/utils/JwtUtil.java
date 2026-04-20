package com.property.management.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    @Autowired
    private JwtConfig jwtConfig;
    
    /**
     * 生成JWT Token
     */
    public String generateToken(Long userId, String username, String role) {
        SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(DateUtil.offsetSecond(new Date(), jwtConfig.getExpiration().intValue()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 解析JWT Token
     */
    public Claims parseToken(String token) {
        if (StrUtil.isBlank(token)) {
            return null;
        }
        
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.replace(jwtConfig.getTokenPrefix(), ""))
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token已过期");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            throw new RuntimeException("Token格式错误");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Token为空");
        }
    }
    
    /**
     * 获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return (Long) claims.get("userId");
        }
        return null;
    }
    
    /**
     * 获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return (String) claims.get("username");
        }
        return null;
    }
    
    /**
     * 获取角色
     */
    public String getRoleFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return (String) claims.get("role");
        }
        return null;
    }
    
    /**
     * 判断Token是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}