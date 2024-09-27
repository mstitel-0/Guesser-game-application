package org.example.authenticationservice.Services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(Base64.getEncoder().encode(JWT_SECRET.getBytes())))
                .compact();
    }

    public String generateEmailConfirmationToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(Keys.hmacShaKeyFor(Base64.getEncoder().encode(JWT_SECRET.getBytes())))
                .compact();
    }

    public String verifyEmailConfirmationToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Base64.getEncoder().encode(JWT_SECRET.getBytes())))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
