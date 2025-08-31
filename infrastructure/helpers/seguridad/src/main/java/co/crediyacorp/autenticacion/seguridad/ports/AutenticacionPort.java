package co.crediyacorp.autenticacion.seguridad.ports;

import org.springframework.security.core.userdetails.UserDetails;


import reactor.core.publisher.Mono;



public interface AutenticacionPort {
    Mono<UserDetails> autenticar(String email, String contrasena);
}
