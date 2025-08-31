package co.crediyacorp.autenticacion.seguridad.adapters;

import co.crediyacorp.autenticacion.model.excepciones.UsernameNotFoundException;
import co.crediyacorp.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.crediyacorp.autenticacion.seguridad.mappers.UserDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceAdapter implements ReactiveUserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final UserDetailsMapper userDetailsMapper;


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return usuarioRepository.buscarUsuarioPorEmail(username)
                .flatMap(userDetailsMapper::toCustomUserDetails)
                .cast(UserDetails.class)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Credenciales incorrectas")));
    }
}
