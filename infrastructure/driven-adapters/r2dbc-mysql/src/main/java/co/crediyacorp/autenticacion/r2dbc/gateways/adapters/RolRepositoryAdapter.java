package co.crediyacorp.autenticacion.r2dbc.gateways.adapters;
import co.crediyacorp.autenticacion.model.rol.Rol;
import co.crediyacorp.autenticacion.model.rol.RolEnum;
import co.crediyacorp.autenticacion.model.rol.gateways.RolRepository;
import co.crediyacorp.autenticacion.r2dbc.entity.RolEntidad;
import co.crediyacorp.autenticacion.r2dbc.gateways.ports.RolR2DBCRepository;
import co.crediyacorp.autenticacion.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;



@Repository
public class RolRepositoryAdapter extends ReactiveAdapterOperations<
        Rol,
        RolEntidad,
        String,
        RolR2DBCRepository
> implements RolRepository {

    public RolRepositoryAdapter(RolR2DBCRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Rol.class));
    }


    @Override
    public Mono<String> obtenerIdRolPorNombre(RolEnum rolEnum) {
        return repository.findIdByNombre(rolEnum.name());

    }
}
