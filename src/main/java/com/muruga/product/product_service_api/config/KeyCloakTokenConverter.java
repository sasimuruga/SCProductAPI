package com.muruga.product.product_service_api.config;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
@RequiredArgsConstructor
public class KeyCloakTokenConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  private static final String GROUPS = "groups";
  private static final String REALM_ACCESS_CLAIM = "realm_access";
  private static final String ROLES_CLAIM = "roles";
  private static final String REALM_RESOURCE_CLAIM = "resource_access";
  private static final String AUTHORITY_PREFIX = "ROLE_";
  private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
      new JwtGrantedAuthoritiesConverter();
  @Value("${spring.security.oauth2.resourceserver.client-id}")
  private String clientId;

  @Override
  public Collection<GrantedAuthority> convert(final Jwt source) {
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AUTHORITY_PREFIX);
    return Stream.concat(
            jwtGrantedAuthoritiesConverter.convert(source).stream(),
            extractResourceRoles(source).stream())
        .collect(Collectors.toSet());
  }

  private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt source) {
    Map<String, Object> claims = source.getClaims();
    LinkedTreeMap<String, ArrayList<String>> realmAccess = (LinkedTreeMap<String, ArrayList<String>>) claims.get(REALM_ACCESS_CLAIM);
    ArrayList<String> roles = realmAccess.get(ROLES_CLAIM);
    Collection<GrantedAuthority> grantedAuthorities = generateAuthoritiesFromClaim(roles);
    LinkedTreeMap<String, LinkedTreeMap<String, ArrayList<String>>>  resourceAccessClaims = (LinkedTreeMap) claims.get(REALM_RESOURCE_CLAIM);
    LinkedTreeMap<String, ArrayList<String>>  resourceClaims = resourceAccessClaims.get(clientId);
    ArrayList<String> resourceRoles = resourceClaims.get(ROLES_CLAIM);
    grantedAuthorities.addAll(generateAuthoritiesFromClaim(resourceRoles));
    return grantedAuthorities;
  }

  Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(AUTHORITY_PREFIX + role.toUpperCase()))
        .collect(Collectors.toList());
  }
}
