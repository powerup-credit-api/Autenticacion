package co.crediyacorp.autenticacion.seguridad.adapters;

import co.crediyacorp.autenticacion.model.usuario.gateways.ClaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


import java.util.UUID;
@Component
@RequiredArgsConstructor
public class ClaveServiceAdapter implements ClaveService {

    private final PasswordEncoder passwordEncoder;
    @Override
    public Mono<String> generarClaveAleatoria() {
        return Mono.fromCallable(
                () -> UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12));
    }

    @Override
    public Mono<String> encriptarClave(String clave) {
        return Mono.fromCallable(() -> passwordEncoder.encode(clave));
    }

    @Override
    public Mono<String> generarClaveEncriptada() {
        return generarClaveAleatoria()
                .flatMap(this::encriptarClave);
    }


}
