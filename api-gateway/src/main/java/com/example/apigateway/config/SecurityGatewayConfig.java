package com.example.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityGatewayConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(authorize -> {
                    authorize.pathMatchers("/actuator")
                            .permitAll();
                    authorize.anyExchange().authenticated();
                })
                .oauth2Login();
        return http.build();
    }
}