package com.example.apigateway.config;

import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {
    private final TokenRelayGatewayFilterFactory filterFactory;

    public ApiGatewayConfiguration(TokenRelayGatewayFilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/get")
                        .uri("http://httpbin.org:80"))
                .route("currency-exchange", p -> p.path("/currency-exchange/**")
                        .filters(f -> f.filter(filterFactory.apply()))
                        .uri("lb://CURRENCY-EXCHANGE-SERVICE"))
                .route(p -> p.path("/currency-conversion/**")
                        .filters(f -> f.filter(filterFactory.apply()))
                        .uri("lb://currency-conversion-service"))
                .route(p -> p.path("/currency-conversion/feing/**")
                        .filters(f -> f.filter(filterFactory.apply()))
                        .uri("lb://currency-conversion-service"))
                .build();
    }
}
