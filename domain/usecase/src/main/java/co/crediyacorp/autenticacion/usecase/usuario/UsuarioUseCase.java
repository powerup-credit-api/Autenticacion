package co.crediyacorp.autenticacion.usecase.usuario;


import co.crediyacorp.autenticacion.model.usuario.Usuario;
import co.crediyacorp.autenticacion.model.usuario.gateways.UsuarioRepository;
import co.crediyacorp.autenticacion.usecase.usuario.excepciones.ValidationException;
import lombok.RequiredArgsConstructor;


import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Log
@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;



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

    public Mono<Void> validarEmailUnico(String email){
        return usuarioRepository.existeUsuarioPorEmail(email).flatMap( existe ->
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
                        : Mono.error(new ValidationException("Email inválido")));
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
