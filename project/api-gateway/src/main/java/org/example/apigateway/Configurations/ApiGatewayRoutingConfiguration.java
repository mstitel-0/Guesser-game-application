package org.example.apigateway.Configurations;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;
import java.time.Duration;

import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class ApiGatewayRoutingConfiguration {

    @Value("${application.services.authentication.baseUrl}")
    private String AUTH_SERVICE_BASE_URL;

    @Bean
    public RouterFunction<ServerResponse> authenticationRoute() {
        return route()
                .route(path("/auth/**"), http(AUTH_SERVICE_BASE_URL))
                .filter(rewritePath("/auth/(?<segment>.*)", "/api/authentication/${segment}"))
                .filter(circuitBreaker("authenticationServiceCircuitBreaker", URI.create("forward:/fallback")))
                .build();
    }

}
