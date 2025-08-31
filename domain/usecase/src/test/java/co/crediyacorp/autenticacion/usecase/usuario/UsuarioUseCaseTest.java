package co.crediyacorp.autenticacion.usecase.usuario;


import co.crediyacorp.autenticacion.model.autenticacion.TokenAutenticacionPort;
import co.crediyacorp.autenticacion.model.excepciones.UsernameNotFoundException;
import co.crediyacorp.autenticacion.model.usuario.Usuario;
import co.crediyacorp.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.crediyacorp.autenticacion.model.excepciones.ValidationException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    private TokenAutenticacionPort tokenAutenticacionPort;

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
    void loguearUsuario_CredencialesValidas_RetornaToken() {

        String email = "usuario@test.com";
        String contrasena = "password123";
        String tokenEsperado = "jwt.token.generado";

        when(tokenAutenticacionPort.executeLogin(email, contrasena))
                .thenReturn(Mono.just(tokenEsperado));


        StepVerifier.create(usuarioUseCase.loguearUsuario(email, contrasena))
                .expectNext(tokenEsperado)
                .verifyComplete();

        verify(tokenAutenticacionPort).executeLogin(email, contrasena);
    }

    @Test
    void loguearUsuario_CredencialesInvalidas_PropagaError() {

        String email = "usuario@test.com";
        String contrasena = "password-incorrecto";

        when(tokenAutenticacionPort.executeLogin(email, contrasena))
                .thenReturn(Mono.error(new RuntimeException("Error de autenticacion")));


        StepVerifier.create(usuarioUseCase.loguearUsuario(email, contrasena))
                .expectError(RuntimeException.class)
                .verify();

        verify(tokenAutenticacionPort).executeLogin(email, contrasena);
    }

    @Test
    void loguearUsuario_EmailNulo_RetornaError() {
        String email = null;
        String contrasena = "password123";

        // Configurar el mock para que devuelva un error cuando reciba null
        when(tokenAutenticacionPort.executeLogin(isNull(), anyString()))
                .thenReturn(Mono.error(new UsernameNotFoundException("Credenciales incorrectas")));

        StepVerifier.create(usuarioUseCase.loguearUsuario(email, contrasena))
                .expectError(UsernameNotFoundException.class)
                .verify();

        verify(tokenAutenticacionPort).executeLogin(isNull(), anyString());
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
