package org.example.authenticationservice.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Base64.getEncoder()
                        .encode(JWT_SECRET.getBytes()));
    }

    public String generateAccessToken(String email, Long userId) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(String email, Long userId) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 12))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateEmailConfirmationToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSecretKey())
                .compact();
    }

    public String verifyEmailConfirmationToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String refreshAccessToken(String refreshToken) {
        try {
            Claims claims = getAllClaims(refreshToken);

            return generateAccessToken(claims.getSubject(), claims.get("userId", Long.class));

        } catch (ExpiredJwtException e) {
            throw new JwtException("Expired refresh token");
        }
    }
}
