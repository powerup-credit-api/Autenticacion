package co.crediyacorp.autenticacion.model.rol.gateways;


import co.crediyacorp.autenticacion.model.rol.RolEnum;
import reactor.core.publisher.Mono;



public interface RolRepository {

    Mono<String> obtenerIdRolPorNombre(RolEnum rolEnum);
}
