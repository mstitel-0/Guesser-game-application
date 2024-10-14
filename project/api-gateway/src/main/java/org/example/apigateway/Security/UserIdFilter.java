package org.example.apigateway.Security;

import org.example.apigateway.Services.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class UserIdFilter{

    private final JwtUtil jwtUtil;

    public UserIdFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public HandlerFilterFunction<ServerResponse, ServerResponse> addUserHeader() {
        return (request, next) -> {
            String header = request.headers().firstHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                Long userId = jwtUtil.extractUserId(token);
                ServerRequest modified = ServerRequest.from(request).header("X-Game-User-Id",
                        String.valueOf(userId)).build();

                return next.handle(modified);
            }
            return next.handle(request);
        };
    }
}
