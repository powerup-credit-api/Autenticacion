package co.crediyacorp.autenticacion.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    CorsWebFilter corsWebFilter(@Value("${cors.allowed-origins:}") String origins) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        if (origins == null || origins.isBlank()) {
            config.setAllowedOrigins(List.of("http://localhost:4200", "http://api-gateway:8080"));
        } else {
            config.setAllowedOrigins(List.of(origins.split(",")));
        }
        config.setAllowedMethods(Arrays.asList("POST", "GET", "PUT"));
        config.setAllowedHeaders(List.of(CorsConfiguration.ALL));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
