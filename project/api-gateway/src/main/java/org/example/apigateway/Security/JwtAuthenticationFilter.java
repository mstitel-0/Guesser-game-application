package org.example.apigateway.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.apigateway.Exceptions.AuthJwtException;
import org.example.apigateway.Services.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final JwtEntryPoint jwtEntryPoint;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, JwtEntryPoint jwtEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.jwtEntryPoint = jwtEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        Claims claims = null;
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                claims = jwtUtil.extractClaims(token);
            }
            if (SecurityContextHolder.getContext().getAuthentication() == null && claims != null) {
                if (jwtUtil.validateToken(token, claims)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(claims.getSubject(), null, Collections.emptyList());

                    authenticationToken.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            jwtEntryPoint.commence(request, response, new AuthJwtException(e.getMessage()));
        }
        filterChain.doFilter(request, response);
    }
}
