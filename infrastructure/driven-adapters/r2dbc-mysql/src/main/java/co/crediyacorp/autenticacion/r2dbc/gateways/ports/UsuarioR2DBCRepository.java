package co.crediyacorp.autenticacion.r2dbc.gateways.ports;

import co.crediyacorp.autenticacion.r2dbc.entity.UsuarioEntidad;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface UsuarioR2DBCRepository extends ReactiveCrudRepository<UsuarioEntidad, String>, ReactiveQueryByExampleExecutor<UsuarioEntidad> {
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByDocumentoIdentidad(String documentoIdentidad);
    Mono<Boolean> existsByDocumentoIdentidadAndEmail(String documentoIdentidad, String email);
    Mono<UsuarioEntidad> findByEmail(String email);

}
