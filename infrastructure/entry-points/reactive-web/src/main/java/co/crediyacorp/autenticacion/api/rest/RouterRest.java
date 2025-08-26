package co.crediyacorp.autenticacion.api.rest;

import co.crediyacorp.autenticacion.api.config.UsuarioPath;
import co.crediyacorp.autenticacion.api.dtos.UsuarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final UsuarioPath usuarioPath;

    @Bean
    @RouterOperation(
            operation = @Operation(
                    operationId = "registrarUsuario",
                    summary = "Registrar nuevo usuario",
                    description = """
            Se debe poder registrar un nuevo solicitante proporcionando sus datos personales.
            Validaciones:
            - nombres, apellidos, correo_electronico y salario_base no pueden ser nulos o vacios.
            - correo_electronico debe ser unico en el sistema.
            - correo_electronico debe tener formato valido.
            - salario_base debe ser numérico entre 0 y 15000000.
        """,
                    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            required = true,
                            description = "Datos del nuevo usuario",
                            content = @Content(
                                    schema = @Schema(implementation = UsuarioDto.class)
                            )
                    ),
                    responses = {
                            @ApiResponse(responseCode = "201", description = "Usuario creado"),
                            @ApiResponse(responseCode = "400", description = "Datos invalidos")
                    }
            )
    )
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(usuarioPath.getRegistrar()), handler::listenRegistrarUsuario);
    }
}
