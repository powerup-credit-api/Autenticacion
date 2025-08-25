package co.crediyacorp.autenticacion.api;

import co.crediyacorp.autenticacion.api.dtos.UsuarioDto;
import co.crediyacorp.autenticacion.api.mappers.UsuarioMapper;
import co.crediyacorp.autenticacion.usecase.usuario.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;


    public Mono<ServerResponse> listenRegistrarUsuario(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(UsuarioDto.class)
                .flatMap(usuarioMapper::toDomain)
                .flatMap(usuarioUseCase::crearUsuario)
                .flatMap(usuarioGuardado-> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(usuarioGuardado));
    }


}
