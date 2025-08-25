package co.crediyacorp.autenticacion.r2dbc.gateways.ports;

import co.crediyacorp.autenticacion.r2dbc.entity.UsuarioEntidad;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;




public interface UsuarioR2DBCRepository extends ReactiveCrudRepository<UsuarioEntidad, String>, ReactiveQueryByExampleExecutor<UsuarioEntidad> {

}
