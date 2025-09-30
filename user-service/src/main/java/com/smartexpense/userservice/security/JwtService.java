package com.smartexpense.userservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secretBase64;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long expirationMs;

    private SecretKey key;

    @PostConstruct
    void init() {
        byte[] decoded = Decoders.BASE64.decode(secretBase64);
        key = Keys.hmacShaKeyFor(decoded); // HS256/HS512-compatible (length based)
    }

    public String generateToken(String subjectEmail) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(subjectEmail)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key) // alg inferred from key size
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String sub = extractUsername(token);
        var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        boolean notExpired = claims.getExpiration() == null || claims.getExpiration().after(new Date());
        return notExpired && sub.equals(userDetails.getUsername());
    }
}