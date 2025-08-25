package co.crediyacorp.autenticacion.r2dbc.gateways.ports;

import co.crediyacorp.autenticacion.r2dbc.entity.RolEntidad;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;




public interface RolR2DBCRepository extends ReactiveCrudRepository<RolEntidad, String>, ReactiveQueryByExampleExecutor<RolEntidad> {

    @Query("SELECT id_rol FROM rol WHERE nombre = :nombre")
    Mono<String> findIdByNombre(String nombre);

}
