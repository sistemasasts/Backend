package com.isacore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AadOAuth2ResourceServerSecurityConfig {

    @Autowired
    private AppProperties appProperties;

    private String jwkSetUri = "https://login.microsoftonline.com/6a4bb97a-6776-4e91-90dd-8d40f0e19091/discovery/v2.0/keys";

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
    @Bean
    public SecurityFilterChain apiFilterChain(final HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(webConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        return http.build();
    }

    CorsConfigurationSource webConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(appProperties.getUrls());
        configuration.setAllowedMethods(Arrays.asList("GET","POST","DELETE","PUT","OPTIONS","HEAD","PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
