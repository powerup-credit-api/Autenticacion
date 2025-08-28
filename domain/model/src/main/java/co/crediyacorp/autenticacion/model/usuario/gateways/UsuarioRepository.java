package co.crediyacorp.autenticacion.model.usuario.gateways;

import co.crediyacorp.autenticacion.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {

    Mono<Usuario> guardarUsuario(Usuario usuario);

    Mono<Boolean> existeUsuarioPorEmail(String email);

    Mono<Boolean> existeUsuarioPorDocumentoIdentidad(String documentoIdentidad);

    Mono<Boolean> existeUsuarioPorEmailYDocumentoIdentidad(String documentoIdentidad, String email);


}
