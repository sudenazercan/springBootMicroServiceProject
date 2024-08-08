package com.example.test.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
public class CustomJwtAuthenticationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        Collection<GrantedAuthority> authorities = defaultGrantedAuthoritiesConverter.convert(jwt);
        if (jwt.getClaimAsMap("realm_access") != null) {
            Collection<GrantedAuthority> realmRoles = ((Collection<String>) jwt.getClaimAsMap("realm_access").get("roles"))
                    .stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
            authorities.addAll(realmRoles);
        }
        // Client roles
        if (jwt.getClaimAsMap("resource_access") != null) {
            Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
            resourceAccess.forEach((resource, access) -> {
                Map<String, Object> accessMap = (Map<String, Object>) access;
                if (accessMap.get("roles") != null) {
                    Collection<GrantedAuthority> clientRoles = ((Collection<String>) accessMap.get("roles"))
                            .stream()
                            .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());
                    authorities.addAll(clientRoles);
                }
            });
        }
        return authorities;
    }
}
