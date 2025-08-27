package co.crediyacorp.autenticacion.api.rest;

import co.crediyacorp.autenticacion.api.dtos.UsuarioDto;
import co.crediyacorp.autenticacion.api.mappers.UsuarioMapper;
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

    }



