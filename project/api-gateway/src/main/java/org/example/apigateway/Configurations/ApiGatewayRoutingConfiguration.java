package org.example.apigateway.Configurations;

import org.example.apigateway.Security.UserIdFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;


import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class ApiGatewayRoutingConfiguration {

    @Value("${application.services.authentication.base-url}")
    private String AUTH_SERVICE_BASE_URL;

    @Value("${application.services.game.base-url}")
    private String GAME_SERVICE_BASE_URL;

    @Value("${application.services.telegram.base-url}")
    private String TELEGRAM_SERVICE_BASE_URL;

    private final UserIdFilter userIdFilter;

    public ApiGatewayRoutingConfiguration(UserIdFilter userIdFilter) {
        this.userIdFilter = userIdFilter;
    }

    @Bean
    public RouterFunction<ServerResponse> authenticationRoute() {
        return route()
                .route(path("/auth/**"), http(AUTH_SERVICE_BASE_URL))
                .filter(rewritePath("/auth/(?<segment>.*)", "/api/authentication/${segment}"))
                .filter(circuitBreaker("authenticationServiceCircuitBreaker", URI.create("forward:/fallback")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> gameRoute() {
        return route()
                .route(path("/game/**"), http(GAME_SERVICE_BASE_URL))
                .filter(rewritePath("/game/(?<segment>.*)", "/api/game/${segment}"))
                .filter(circuitBreaker("gameServiceCircuitBreaker", URI.create("forward:/fallback")))
                .filter(userIdFilter.addUserHeader())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> apiRoute() {
        return route()
                .route(path("/telegram"), http(TELEGRAM_SERVICE_BASE_URL))
                .filter(rewritePath("/telegram", "/api/telegram/webhook/process"))
                .filter(circuitBreaker("telegramServiceCircuitBreaker", URI.create("forward:/fallback")))
                .build();
    }
}
