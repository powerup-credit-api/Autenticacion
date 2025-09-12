package co.crediyacorp.autenticacion.usecase.usuario.usecases;


import co.crediyacorp.autenticacion.model.autenticacion.TokenAutenticacionPort;
import co.crediyacorp.autenticacion.model.usuario.Usuario;
import co.crediyacorp.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.crediyacorp.autenticacion.model.excepciones.ValidationException;
import lombok.RequiredArgsConstructor;


import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Log
@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    private final TokenAutenticacionPort tokenAutenticacionPort;



    public Mono<Usuario> crearUsuario(Usuario usuario) {
        return validarCamposVacios(usuario)
                .then(validarEmailFormato(usuario.getEmail().trim().toLowerCase()))
                .then(validarEmailUnico(usuario.getEmail()))
                .then(validarDocumentoIdentidadUnico(usuario.getDocumentoIdentidad()))
                .then(validarSalarioBase(usuario.getSalarioBase()))
                .then(Mono.just(usuario))
                .doOnNext(u -> {
                    u.setIdUsuario(UUID.randomUUID().toString());
                    log.info("Usuario creado" + u.getEmail());

                })
                .flatMap(usuarioRepository::guardarUsuario)
                .doOnSuccess(u -> log.info("Usuario guardado correctamente con ID " + u.getIdUsuario()))
                .doOnError(e -> log.severe("Error al crear el usuario: " + e.getMessage()));
    }

    public Flux<BigDecimal> obtenerSalariosBasePorEmails(List<String> email){
        return Flux.fromIterable(email)
                .flatMapSequential(usuarioRepository::obtenerSalarioBasePorEmail)
                .switchIfEmpty(Mono.error(new ValidationException("El usuario no existe en la base de datos.")));
    }

    public Mono<BigDecimal> obtenerSalarioBasePorEmail(String email){
        return usuarioRepository.obtenerSalarioBasePorEmail(email)
                .switchIfEmpty(Mono.error(new ValidationException("El usuario no existe en la base de datos")));
    }

    public Mono<Boolean> validarUsuarioEnDb(String email, String documentoIdentidad){
        return usuarioRepository.existeUsuarioPorEmailYDocumentoIdentidad(documentoIdentidad,email)
                .flatMap(existe -> Boolean.TRUE.equals(existe)
                        ? Mono.just(true)
                        : Mono.error(new ValidationException("El usuario no existe en la base de datos"))
                );

    }

    public Mono<String> loguearUsuario(String email, String contrasena){
        return tokenAutenticacionPort.executeLogin(email,contrasena)
                .doOnSuccess(token -> log.info("Usuario logueado correctamente con email " + email))
                .doOnError(e -> log.severe("Error al loguear el usuario: " + e.getMessage()));
    }




    public Mono<Void> validarEmailUnico(String email){
        return usuarioRepository.existeUsuarioPorEmail(email.trim().toLowerCase()).flatMap( existe ->
                Boolean.TRUE.equals(existe) ? Mono.error(new ValidationException("El email ya esta en uso"))
                        : Mono.empty()
        );
    }

    public Mono<Void> validarDocumentoIdentidadUnico(String documentoIdentidad){
        return usuarioRepository.existeUsuarioPorDocumentoIdentidad(documentoIdentidad).flatMap( existe ->
                Boolean.TRUE.equals(existe) ? Mono.error(new ValidationException("El documento de identidad ya esta en uso"))
                        : Mono.empty()
        );
    }

    public Mono<Void> validarSalarioBase(BigDecimal salarioBase){
        return Mono.just(salarioBase).flatMap( salario ->
                salario.compareTo(BigDecimal.ZERO) < 0 || salario.compareTo(new BigDecimal("15000000")) > 0
                        ? Mono.error(new ValidationException("El salario base debe ser mayor que cero"))
                        : Mono.empty()
        );
    }



    public Mono<Void> validarEmailFormato(String email) {
        return Mono.just(email)
                .flatMap(e -> e.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
                        ? Mono.empty()
                        : Mono.error(new ValidationException("Email invalido")));
    }

    public Mono<Void> validarCamposVacios(Usuario usuario) {
        return Flux.concat(
                        validarCampo(usuario.getNombres(), "El nombre no puede estar vacio"),
                        validarCampo(usuario.getApellidos(), "El apellido no puede estar vacio"),
                        validarCampo(usuario.getEmail(), "El email no puede estar vacio"),
                        validarCampo(usuario.getDocumentoIdentidad(), "El documento de identidad no puede estar vacio"),
                        usuario.getSalarioBase() == null
                                ? Mono.error(new ValidationException("El salario base no puede estar vacio"))
                                : Mono.empty()
                )
                .next();
    }

    private Mono<Void> validarCampo(String valor, String mensajeError) {
        return (valor == null || valor.trim().isEmpty())
                ? Mono.error(new ValidationException(mensajeError))
                : Mono.empty();
    }



}
