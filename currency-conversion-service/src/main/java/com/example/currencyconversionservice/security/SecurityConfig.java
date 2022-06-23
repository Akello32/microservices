package com.example.currencyconversionservice.security;

import com.example.currencyexchangeservice.security.GoogleTokenIntrospector;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(authorizeExchangeSpec -> {
                    authorizeExchangeSpec
                            .antMatchers("/actuator").permitAll();
                    authorizeExchangeSpec
                            .anyRequest().authenticated();
                })
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::opaqueToken);
        return http.build();
    }

    @Bean
    public OpaqueTokenIntrospector introspector() {
        return new GoogleTokenIntrospector("https://oauth2.googleapis.com/tokeninfo");
    }
}
