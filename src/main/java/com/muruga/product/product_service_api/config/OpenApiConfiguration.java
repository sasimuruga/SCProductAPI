package com.muruga.product.product_service_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class OpenApiConfiguration {

  @Bean
  public OpenAPI customOpenAPI(
      @Value("${springdoc.version}") String appVersion, @Value("${auth-url}") String authUrl) {
    Server server1 = new Server();
    server1.setUrl("https://dev.example.idp.com");
    Server server2 = new Server();
    server2.setUrl("http://sit.example.idp.com");
    return new OpenAPI()
        .servers(Arrays.asList(server1, server2))
        .components(
            new Components()
                .addSecuritySchemes(
                    "spring_oauth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                        .description("OAuth2 Flow")
                        .flows(
                            new OAuthFlows()
                                .implicit(
                                    new OAuthFlow()
                                        .authorizationUrl(authUrl)
                                        .refreshUrl(authUrl + "/token")
                                        .tokenUrl(authUrl + "/token")
                                        .scopes(new Scopes())))))
        .info(
            new Info()
                .title("Product Service API")
                .version(appVersion)
                .description("This is an application API for Product Services.")
                .termsOfService("http://swagger.io/terms/")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
        .security(Arrays.asList(new SecurityRequirement().addList("spring_oauth")));
  }
}
