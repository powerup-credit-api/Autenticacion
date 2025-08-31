package co.crediyacorp.autenticacion.seguridad.adapters;

import co.crediyacorp.autenticacion.model.excepciones.BadCredentialsCustomException;
import co.crediyacorp.autenticacion.seguridad.ports.AutenticacionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class AutenticacionAdapter implements AutenticacionPort {
    private final ReactiveAuthenticationManager authManager;

    @Override
    public Mono<UserDetails> autenticar(String email, String password) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(email, password);
        return authManager.authenticate(authToken)
                .map(Authentication::getPrincipal)
                .cast(UserDetails.class)
                .onErrorMap(e -> e instanceof BadCredentialsException ?
                            new BadCredentialsCustomException("Credenciales incorrectas") :
                            e
                );
    }
}
