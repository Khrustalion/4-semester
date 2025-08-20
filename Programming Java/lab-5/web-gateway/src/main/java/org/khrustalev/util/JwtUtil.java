package org.khrustalev.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.khrustalev.dto.AuthResponseDto;
import org.khrustalev.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    private Key secretKey;
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public Key getSecretKey() {
        return secretKey;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @NotNull
    public AuthResponseDto getAuthResponseDto(@NotNull UserDto user) {
        String token = null;
        String jti = UUID.randomUUID().toString();

        try {
            token = Jwts.builder()
                    .setSubject(user.getName())
                    .setId(jti)
                    .claim("userId", user.getId())
                    .claim("role", user.getRole().name())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + this.getExpiration()))
                    .signWith(this.getSecretKey())
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new AuthResponseDto(token, user.getName(), user.getRole().name(), user.getId(), user.getOwnerId());
    }
}