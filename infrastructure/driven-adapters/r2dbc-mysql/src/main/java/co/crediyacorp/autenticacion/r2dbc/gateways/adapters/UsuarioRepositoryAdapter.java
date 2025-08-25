package co.crediyacorp.autenticacion.r2dbc.gateways.adapters;

import co.crediyacorp.autenticacion.model.usuario.Usuario;
import co.crediyacorp.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.crediyacorp.autenticacion.r2dbc.gateways.ports.UsuarioR2DBCRepository;
import co.crediyacorp.autenticacion.r2dbc.entity.UsuarioEntidad;
import co.crediyacorp.autenticacion.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;


@Repository
public class UsuarioRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntidad,
        String,
        UsuarioR2DBCRepository
> implements UsuarioRepository {

    private final TransactionalOperator transactionalOperator;

    public UsuarioRepositoryAdapter(UsuarioR2DBCRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Usuario> guardarUsuario(Usuario usuario){
        return super.save(usuario)
                .as(transactionalOperator::transactional);

    }


    @Override
    public Mono<Boolean> existeUsuarioPorEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Mono<Boolean> existeUsuarioPorDocumentoIdentidad(String documentoIdentidad) {
        return repository.existsByDocumentoIdentidad(documentoIdentidad);
    }





}
