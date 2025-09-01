package co.crediyacorp.autenticacion.seguridad.adapters.validacion;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.context.annotation.Bean;

@Configuration
public class HeaderAutenticacionFilterConfig  {

    @Bean
    public AuthenticationWebFilter headerAuthFilter(
            @Qualifier("headerReactiveAuthenticationManager") ReactiveAuthenticationManager headerReactiveAuthenticationManager,
            ServerAuthenticationConverter headerAuthenticationConverter) {

        AuthenticationWebFilter headerAuthFilter =
                new AuthenticationWebFilter(headerReactiveAuthenticationManager);
        headerAuthFilter.setServerAuthenticationConverter(headerAuthenticationConverter);
        return headerAuthFilter;
    }

}
