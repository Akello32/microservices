package com.example.apigateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApiGatewayController {

    private final Logger logger = LoggerFactory.getLogger(ApiGatewayController.class);

    @Autowired
    private ReactiveOAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @GetMapping()
    public Map getTest(OAuth2AuthenticationToken authentication) {
        List<OAuth2AccessToken> tokens = new ArrayList<>();
        logger.info("Oauth Token {}", authentication.getCredentials());
        Mono<OAuth2AuthorizedClient> google = oAuth2AuthorizedClientService
                .loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        google.map(OAuth2AuthorizedClient::getAccessToken).subscribe(tokens::add);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("tokens", tokens);
        attributes.putAll(authentication.getPrincipal().getAttributes());
        attributes.put("Authorities", authentication.getPrincipal().getAuthorities());

        return attributes;
    }
}
