package org.example.apigateway.Configurations;

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
public class ApiGatewayRouting {

    @Bean
    public RouterFunction<ServerResponse> authenticationRoute() {
        return route()
                .route(path("/auth/**"), http("http://localhost:8080"))
                .filter(rewritePath("/auth/(?<segment>.*)", "/api/authentication/${segment}"))
                .filter(circuitBreaker("authenticationServiceCircuitBreaker", URI.create("forward:/fallback")))
                .build();
    }

}
