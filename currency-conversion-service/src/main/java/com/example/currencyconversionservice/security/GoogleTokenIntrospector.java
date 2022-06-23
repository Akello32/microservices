package com.example.currencyexchangeservice.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class GoogleTokenIntrospector implements OpaqueTokenIntrospector {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String introspectionUri;

    private final Logger logger = LoggerFactory.getLogger(GoogleTokenIntrospector.class);

    public GoogleTokenIntrospector(String introspectionUri) {
        this.introspectionUri = introspectionUri;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        RequestEntity<?> requestEntity = buildRequest(token);
        try {
            ResponseEntity<Map<String, Object>> responseEntity =
                    this.restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
                    });

            Map<String, Object> body = responseEntity.getBody();
            if (body == null || body.isEmpty()) {
                throw new OAuth2IntrospectionException(
                        "Introspection endpoint response was invalid, as no json body was provided");
            }

            long remainedLive = TimeUnit.SECONDS.toMillis(Long.parseLong((String) body.get("expires_in")));
            Instant iat = Instant.now().minusMillis(remainedLive);
            body.put(OAuth2TokenIntrospectionClaimNames.IAT, iat);

            long exp = TimeUnit.SECONDS.toMillis(Long.parseLong((String) body.get("exp")));
            body.put(OAuth2TokenIntrospectionClaimNames.EXP, Instant.ofEpochSecond(exp));

            String scopeString = (String) body.get("scope");
            List<GrantedAuthority> authorities = Arrays.stream(scopeString.split(" "))
                    .map("SCOPE_"::concat)
                    .map(scope -> {
                        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(scope);
                        return (GrantedAuthority) simpleGrantedAuthority;
                    })
                    .toList();

            return new OAuth2IntrospectionAuthenticatedPrincipal((String) body.get("sub"), body, authorities);
        } catch (Exception ex) {
            throw new BadOpaqueTokenException(ex.getMessage(), ex);
        }
    }

    private RequestEntity<?> buildRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("access_token", token);

        return new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(introspectionUri));
    }
}