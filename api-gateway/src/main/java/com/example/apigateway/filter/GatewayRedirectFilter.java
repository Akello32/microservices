package com.example.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class GatewayRedirectFilter extends AbstractGatewayFilterFactory<String> {

    private final Logger logger = LoggerFactory.getLogger(GatewayRedirectFilter.class);

    private final String LOGIN_REDIRECT = "/login/oauth2/code/github";
    private final String CODE_PARAMETER_NAME = "code";

    @Override
    public GatewayFilter apply(String config) {
        logger.info("GatewayRedirectFilter");
        return ((exchange, chain) -> {
            if (exchange.getRequest().getPath().value().equals(LOGIN_REDIRECT)) {
                if (exchange.getRequest().getQueryParams().containsKey(CODE_PARAMETER_NAME)) {
                    String code = exchange.getRequest().getQueryParams().getFirst(CODE_PARAMETER_NAME);

                    Map<String, String> uriVariables = new HashMap<>();
                    uriVariables.put(CODE_PARAMETER_NAME, code);
                    uriVariables.put("client_id", "0399f32da8a544ed0a1e");
                    uriVariables.put("client_secret", "d1c26f3307fd4532e2ecc178667ecafa00f4ac71");

                    ResponseEntity<String> responseEntity = new RestTemplate()
                            .getForEntity("https://github.com/login/oauth/access_token", String.class, uriVariables);
                }
            }


            return chain.filter(exchange);
        });
    }
}
