package co.crediyacorp.autenticacion.api.rest;

import co.crediyacorp.autenticacion.api.dtos.UsuarioDto;
import co.crediyacorp.autenticacion.api.mappers.UsuarioMapper;
import co.crediyacorp.autenticacion.seguridad.dtos.AuthResponse;
import co.crediyacorp.autenticacion.seguridad.dtos.LoginRequest;
import co.crediyacorp.autenticacion.usecase.usuario.transaction_usecase.ExecuteUsuarioUseCase;
import co.crediyacorp.autenticacion.usecase.usuario.usecases.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;


import java.util.List;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ExecuteUsuarioUseCase executeUsuarioUseCase;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioUseCase usuarioUseCase;


    public Mono<ServerResponse> listenRegistrarUsuario(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(UsuarioDto.class)
                .flatMap(usuarioMapper::toDomain)
                .flatMap(executeUsuarioUseCase::executeGuardarUsuario)
                .flatMap(usuarioGuardado ->
                        usuarioMapper.fromEntityToUsuarioResponseDto(usuarioGuardado)
                                .flatMap(dto -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(dto))
                );

    }


    public Mono<ServerResponse> listenValidarUsuario(ServerRequest request) {
        String email = request.queryParam("email")
                .orElseThrow(() -> new IllegalArgumentException("El parámetro 'email' es obligatorio"));

        String documentoIdentidad = request.queryParam("documentoIdentidad")
                .orElseThrow(() -> new IllegalArgumentException("El parámetro 'documentoIdentidad' es obligatorio"));

        return executeUsuarioUseCase.executeValidarUsuarioEnDb(email, documentoIdentidad)
                .flatMap(existe -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(existe));
    }

    public Mono<ServerResponse> listenObtenerSalariosBasePorEmails(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .flatMapMany(usuarioUseCase::obtenerSalariosBasePorEmails)
                .collectList()
                .flatMap(salarios -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(salarios));
    }


    public Mono<ServerResponse> listenLoginUsuario(ServerRequest request) {

        return request.bodyToMono(LoginRequest.class)
                .flatMap(dto -> executeUsuarioUseCase.executeloguearUsuario(dto.email(), dto.contrasena()))
                .map(token -> new AuthResponse(token, "Bearer"))
                .flatMap(authResponse -> ServerResponse.ok().bodyValue(authResponse));
    }

}


