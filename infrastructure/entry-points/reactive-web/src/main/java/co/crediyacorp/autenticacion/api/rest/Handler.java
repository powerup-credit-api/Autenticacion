package co.crediyacorp.autenticacion.api.rest;

import co.crediyacorp.autenticacion.api.dtos.UsuarioDto;
import co.crediyacorp.autenticacion.api.mappers.UsuarioMapper;
import co.crediyacorp.autenticacion.seguridad.dtos.AuthResponse;
import co.crediyacorp.autenticacion.seguridad.dtos.LoginRequest;
import co.crediyacorp.autenticacion.usecase.usuario.transaction_usecase.ExecuteUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ExecuteUsuarioUseCase executeUsuarioUseCase;
    private final UsuarioMapper usuarioMapper;


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

    public Mono<ServerResponse> listenLoginUsuario(ServerRequest request) {

        return request.bodyToMono(LoginRequest.class)
                .flatMap(dto -> executeUsuarioUseCase.executeloguearUsuario(dto.email(), dto.contrasena()))
                .map(token -> new AuthResponse(token, "Bearer"))
                .flatMap(authResponse -> ServerResponse.ok().bodyValue(authResponse));
    }

}


