package com.smartexpense.budgetservice.security;

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
    public void init() {
        byte[] decoded = Decoders.BASE64.decode(secretBase64);
        key = Keys.hmacShaKeyFor(decoded);
    }

    public String generateToken(String subject) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String subject = extractUsername(token);
        var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        boolean notExpired = claims.getExpiration() == null || claims.getExpiration().after(new Date());
        return notExpired && subject.equals(userDetails.getUsername());
    }
}
