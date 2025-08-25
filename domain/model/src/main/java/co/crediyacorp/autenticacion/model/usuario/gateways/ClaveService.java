package co.crediyacorp.autenticacion.model.usuario.gateways;

import reactor.core.publisher.Mono;

public interface ClaveService {
    Mono<String> generarClaveAleatoria();
    Mono<String> encriptarClave(String clave);
    Mono<String> generarClaveEncriptada();
}
