package co.crediyacorp.autenticacion.r2dbc.gateways.adapters;

import co.crediyacorp.autenticacion.model.usuario.Usuario;
import co.crediyacorp.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.crediyacorp.autenticacion.r2dbc.gateways.ports.UsuarioR2DBCRepository;
import co.crediyacorp.autenticacion.r2dbc.entity.UsuarioEntidad;
import co.crediyacorp.autenticacion.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


@Repository
public class UsuarioRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntidad,
        String,
        UsuarioR2DBCRepository
> implements UsuarioRepository {



    public UsuarioRepositoryAdapter(UsuarioR2DBCRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));

    }

    @Override
    public Mono<Usuario> guardarUsuario(Usuario usuario){
        return super.save(usuario);

    }


    @Override
    public Mono<Boolean> existeUsuarioPorEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Mono<Boolean> existeUsuarioPorDocumentoIdentidad(String documentoIdentidad) {
        return repository.existsByDocumentoIdentidad(documentoIdentidad);
    }

    @Override
    public Mono<Boolean> existeUsuarioPorEmailYDocumentoIdentidad(String documentoIdentidad, String email) {
        return repository.existsByDocumentoIdentidadAndEmail(documentoIdentidad,email);
    }


    @Override
    public Mono<Usuario> buscarUsuarioPorEmail(String email) {
        return repository.findByEmail(email)
                .map(this::toEntity);
    }

    @Override
    public Mono<BigDecimal> obtenerSalarioBasePorEmail(String email) {
        return repository.findSalarioBaseByEmail(email);
    }

}



