package com.muruga.product.product_service_api.config;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(Customizer.withDefaults())
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(
            authorize -> authorize
                .requestMatchers(
                    "/acutator/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/product/**")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated())
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt(
                jwt ->
                    jwt.jwtAuthenticationConverter(
                        jwtAuthenticationConverter(new KeyCloakTokenConverter()))))
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter(
      Converter<Jwt, Collection<GrantedAuthority>> customConverter) {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(customConverter);
    return jwtAuthenticationConverter;
  }
}
