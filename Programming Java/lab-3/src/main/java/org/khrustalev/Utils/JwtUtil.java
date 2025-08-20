package org.khrustalev.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {
    private static final String SECRET = "my-secret-key-which-can-be-used-in-the-context";
    private Key secret = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private long expiration;

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}