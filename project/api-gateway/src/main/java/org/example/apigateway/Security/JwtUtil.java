package org.example.apigateway.Security;

import io.jsonwebtoken.Claims;
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
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(JWT_SECRET.getBytes()));
    }

    public Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Boolean validateToken(String token, Claims claims) {
        String username = extractClaims(token).getSubject();
        return !isExpired(token) && claims.getSubject().equals(username);
    }

    public Boolean isExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
