package org.example.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Value("${telegram.bot.gateway.host}")
    private String gatewayHost;

    @Bean
    public RestClient restClient() {
        System.out.println("GATEWAY URL: " + gatewayHost);
        return RestClient.builder()
                .baseUrl(gatewayHost)
                .build();
    }
}
