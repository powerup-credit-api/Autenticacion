package co.crediyacorp.autenticacion.model.autenticacion;

import reactor.core.publisher.Mono;

public interface TokenAutenticacionPort {
    Mono<String> executeLogin(String email, String password);
}