package co.crediyacorp.autenticacion.r2dbc;

import co.crediyacorp.autenticacion.model.usuario.Usuario;
import co.crediyacorp.autenticacion.r2dbc.gateways.adapters.UsuarioRepositoryAdapter;
import co.crediyacorp.autenticacion.r2dbc.gateways.ports.UsuarioR2DBCRepository;
import co.crediyacorp.autenticacion.r2dbc.entity.UsuarioEntidad;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioR2DBCRepositoryAdapterTest {

    @InjectMocks
    UsuarioRepositoryAdapter repositoryAdapter;

    @Mock
    UsuarioR2DBCRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustFindValueById() {
        String id = UUID.randomUUID().toString();
        UsuarioEntidad entidad = buildUsuarioEntidad(id);
        Usuario usuario = buildUsuario(id);

        when(repository.findById(id)).thenReturn(Mono.just(entidad));
        when(mapper.map(entidad, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.findById(id);

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        String id = UUID.randomUUID().toString();
        UsuarioEntidad entidad = buildUsuarioEntidad(id);
        Usuario usuario = buildUsuario(id);

        when(repository.findAll()).thenReturn(Flux.just(entidad));
        when(mapper.map(entidad, Usuario.class)).thenReturn(usuario);

        Flux<Usuario> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        String id = UUID.randomUUID().toString();
        UsuarioEntidad entidad = buildUsuarioEntidad(id);
        Usuario usuario = buildUsuario(id);

        when(repository.save(entidad)).thenReturn(Mono.just(entidad));
        when(mapper.map(entidad, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.save(usuario);

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();
    }

    private UsuarioEntidad buildUsuarioEntidad(String id) {
        UsuarioEntidad u = new UsuarioEntidad();
        u.setIdUsuario(id);
        u.setNombres("Juan");
        u.setApellidos("Pérez");
        u.setEmail("juan@test.com");
        u.setDocumentoIdentidad("123456");
        u.setTelefono("3001234567");
        u.setDireccion("Calle falsa 123");
        u.setSalarioBase(BigDecimal.valueOf(2000));
        u.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        u.setContrasena("secreta");
        u.setRolId("ADMIN");
        return u;
    }

    private Usuario buildUsuario(String id) {
        return new Usuario(id, "Juan", "Pérez", "juan@test.com",
                "123456", "3001234567", "Calle falsa 123",
                BigDecimal.valueOf(2000), LocalDate.of(1990, 1, 1),
                "secreta", "f47ac10b-58cc-4372-a567-0e02b2c3d479");
    }
}