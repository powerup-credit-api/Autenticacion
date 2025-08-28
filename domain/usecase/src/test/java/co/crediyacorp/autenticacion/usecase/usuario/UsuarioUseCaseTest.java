package co.crediyacorp.autenticacion.usecase.usuario;


import co.crediyacorp.autenticacion.model.usuario.Usuario;
import co.crediyacorp.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.crediyacorp.autenticacion.usecase.usuario.excepciones.ValidationException;
import co.crediyacorp.autenticacion.usecase.usuario.usecases.UsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @InjectMocks
    UsuarioUseCase usuarioUseCase;


    @Test
    void crearUsuario_deberiaGuardarUsuarioExitosamente() {

        Usuario usuario = Usuario.builder()
                .nombres("Juan")
                .apellidos("Pérez")
                .email("juan@test.com")
                .documentoIdentidad("123456789")
                .telefono("3001234567")
                .direccion("Calle 123")
                .salarioBase(new BigDecimal("5000"))
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .rolId("f47ac10b-58cc-4372-a567-0e02b2c3d479")
                .build();

        when(usuarioRepository.existeUsuarioPorEmail(usuario.getEmail())).thenReturn(Mono.just(false));
        when(usuarioRepository.existeUsuarioPorDocumentoIdentidad(usuario.getDocumentoIdentidad())).thenReturn(Mono.just(false));


        when(usuarioRepository.guardarUsuario(any(Usuario.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));


        Mono<Usuario> resultado = usuarioUseCase.crearUsuario(usuario);

        StepVerifier.create(resultado)
                .expectNextMatches(u ->
                        u.getEmail().equals("juan@test.com") &&
                                u.getIdUsuario() != null &&
                                u.getSalarioBase().equals(new BigDecimal("5000"))
                )
                .verifyComplete();
    }
    @Test
    void validarUsuarioEnDb_deberiaRetornarTrueSiExiste() {
        String email = "juan@test.com";
        String documento = "123456789";

        when(usuarioRepository.existeUsuarioPorEmailYDocumentoIdentidad(documento, email))
                .thenReturn(Mono.just(true));

        StepVerifier.create(usuarioUseCase.validarUsuarioEnDb(email, documento))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void validarUsuarioEnDb_deberiaFallarSiNoExiste() {
        String email = "juan@test.com";
        String documento = "123456789";

        when(usuarioRepository.existeUsuarioPorEmailYDocumentoIdentidad(documento, email))
                .thenReturn(Mono.just(false));

        StepVerifier.create(usuarioUseCase.validarUsuarioEnDb(email, documento))
                .expectErrorMatches(e -> e instanceof ValidationException &&
                        e.getMessage().equals("El usuario no existe en la base de datos"))
                .verify();
    }


    @Test
    void validarEmailUnico_deberiaFallarSiYaExisteUnDocumento() {

        Usuario usuario = Usuario.builder()
                .nombres("Juan")
                .apellidos("Pérez")
                .email("juan@test.com")
                .documentoIdentidad("123456789")
                .telefono("3001234567")
                .direccion("Calle 123")
                .salarioBase(new BigDecimal("5000"))
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .rolId("f47ac10b-58cc-4372-a567-0e02b2c3d479")
                .build();

        when(usuarioRepository.existeUsuarioPorEmail(usuario.getEmail()))
                .thenReturn(Mono.just(true));


        StepVerifier.create(usuarioUseCase.validarEmailUnico(usuario.getEmail()))
                .expectErrorMatches(throwable ->
                        throwable instanceof ValidationException &&
                                throwable.getMessage().equals("El email ya esta en uso")
                )
                .verify();
    }



    @Test
    void validarDocumentoIdentidadUnico_deberiaFallarSiYaExisteUnDocumento() {

        Usuario usuario = Usuario.builder()
                .nombres("Juan")
                .apellidos("Pérez")
                .email("juan@test.com")
                .documentoIdentidad("123456789")
                .telefono("3001234567")
                .direccion("Calle 123")
                .salarioBase(new BigDecimal("5000"))
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .rolId("f47ac10b-58cc-4372-a567-0e02b2c3d479")
                .build();

        when(usuarioRepository.existeUsuarioPorDocumentoIdentidad(usuario.getDocumentoIdentidad()))
                .thenReturn(Mono.just(true));


        StepVerifier.create(usuarioUseCase.validarDocumentoIdentidadUnico(usuario.getDocumentoIdentidad()))
                .expectErrorMatches(throwable ->
                        throwable instanceof ValidationException &&
                                throwable.getMessage().equals("El documento de identidad ya esta en uso")
                )
                .verify();
    }

    @Test
    void validarSalarioBase_deberiaFallarSiEsNegativo() {
        StepVerifier.create(usuarioUseCase.validarSalarioBase(new BigDecimal("-1000")))
                .expectErrorMatches(e -> e instanceof ValidationException &&
                        e.getMessage().equals("El salario base debe ser mayor que cero"))
                .verify();
    }

    @Test
    void validarSalarioBase_deberiaFallarSiEsMayorAlMaximo() {
        StepVerifier.create(usuarioUseCase.validarSalarioBase(new BigDecimal("20000000")))
                .expectErrorMatches(e -> e instanceof ValidationException &&
                        e.getMessage().equals("El salario base debe ser mayor que cero"))
                .verify();
    }

    @Test
    void validarSalarioBase_deberiaPasarSiEsValido() {
        StepVerifier.create(usuarioUseCase.validarSalarioBase(new BigDecimal("5000000")))
                .verifyComplete();
    }

    @Test
    void validarEmailFormato_deberiaFallarSiFormatoEsInvalido() {
        StepVerifier.create(usuarioUseCase.validarEmailFormato("correo-invalido"))
                .expectErrorMatches(e -> e instanceof ValidationException &&
                        e.getMessage().equals("Email inválido"))
                .verify();
    }

    @Test
    void validarEmailFormato_deberiaPasarSiFormatoEsValido() {
        StepVerifier.create(usuarioUseCase.validarEmailFormato("test@dominio.com"))
                .verifyComplete();
    }

    @Test
    void validarCamposVacios_deberiaFallarSiNombreEsNulo() {
        Usuario usuario = Usuario.builder()
                .nombres(null)
                .apellidos("Perez")
                .email("test@dominio.com")
                .documentoIdentidad("12345")
                .salarioBase(new BigDecimal("1000"))
                .fechaNacimiento(LocalDate.now())
                .build();

        StepVerifier.create(usuarioUseCase.validarCamposVacios(usuario))
                .expectErrorMatches(e -> e instanceof ValidationException &&
                        e.getMessage().equals("El nombre no puede estar vacio"))
                .verify();
    }

    @Test
    void validarCamposVacios_deberiaFallarSiSalarioEsNulo() {
        Usuario usuario = Usuario.builder()
                .nombres("Juan")
                .apellidos("Perez")
                .email("test@dominio.com")
                .documentoIdentidad("12345")
                .salarioBase(null)
                .fechaNacimiento(LocalDate.now())
                .build();

        StepVerifier.create(usuarioUseCase.validarCamposVacios(usuario))
                .expectErrorMatches(e -> e instanceof ValidationException &&
                        e.getMessage().equals("El salario base no puede estar vacio"))
                .verify();
    }

    @Test
    void validarCamposVacios_deberiaPasarSiTodosSonValidos() {
        Usuario usuario = Usuario.builder()
                .nombres("Juan")
                .apellidos("Perez")
                .email("test@dominio.com")
                .documentoIdentidad("12345")
                .salarioBase(new BigDecimal("5000"))
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .build();

        StepVerifier.create(usuarioUseCase.validarCamposVacios(usuario))
                .verifyComplete();
    }

}
