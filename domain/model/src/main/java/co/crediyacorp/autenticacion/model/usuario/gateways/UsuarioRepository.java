package co.crediyacorp.autenticacion.model.usuario.gateways;

import co.crediyacorp.autenticacion.model.usuario.Usuario;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface UsuarioRepository {

    Mono<Usuario> guardarUsuario(Usuario usuario);

    Mono<Boolean> existeUsuarioPorEmail(String email);

    Mono<Boolean> existeUsuarioPorDocumentoIdentidad(String documentoIdentidad);

    Mono<Boolean> existeUsuarioPorEmailYDocumentoIdentidad(String documentoIdentidad, String email);


    Mono<Usuario> buscarUsuarioPorEmail(String idUsuario);

    Mono<BigDecimal> obtenerSalarioBasePorEmail(String email);
}
