package com.property.management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    @Value("${jwt.token-header}")
    private String tokenHeader;
    
    @Value("${jwt.token-prefix}")
    private String tokenPrefix;
    
    public String getSecret() {
        return secret;
    }
    
    public Long getExpiration() {
        return expiration;
    }
    
    public String getTokenHeader() {
        return tokenHeader;
    }
    
    public String getTokenPrefix() {
        return tokenPrefix;
    }
}