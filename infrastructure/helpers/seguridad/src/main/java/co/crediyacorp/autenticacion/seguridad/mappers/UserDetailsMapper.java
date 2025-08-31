package co.crediyacorp.autenticacion.seguridad.mappers;

import co.crediyacorp.autenticacion.model.rol.gateways.RolRepository;
import co.crediyacorp.autenticacion.model.usuario.Usuario;

import co.crediyacorp.autenticacion.seguridad.dtos.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class UserDetailsMapper {

    private final RolRepository rolRepository;


    public Mono<CustomUserDetails> toCustomUserDetails(Usuario usuario) {
        return rolRepository.obtenerNombreRolPorId(usuario.getRolId())
                .map(rolNombre -> new CustomUserDetails(
                        usuario.getEmail(),
                        usuario.getContrasena(),
                        rolNombre
                ));
    }
}
