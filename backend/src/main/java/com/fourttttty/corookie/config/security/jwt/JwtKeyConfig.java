package com.fourttttty.corookie.config.security.jwt;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
public class JwtKeyConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}