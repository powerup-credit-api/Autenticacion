package co.crediyacorp.autenticacion.seguridad.adapters;

import co.crediyacorp.autenticacion.model.autenticacion.TokenAutenticacionPort;
import co.crediyacorp.autenticacion.seguridad.configuration.jwt.JwtProvider;
import co.crediyacorp.autenticacion.seguridad.dtos.CustomUserDetails;
import co.crediyacorp.autenticacion.seguridad.ports.AutenticacionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class TokenAutenticacionPortAdapter implements TokenAutenticacionPort {

    private final AutenticacionPort authenticationPort;
    private final JwtProvider jwtProvider;

    public Mono<String> executeLogin(String email, String contrasena) {
        return authenticationPort.autenticar(email,contrasena)
                .cast(CustomUserDetails.class)
                .flatMap(user -> jwtProvider.generateToken(user.getUsername(), user.getRol())
                );
    }

}
