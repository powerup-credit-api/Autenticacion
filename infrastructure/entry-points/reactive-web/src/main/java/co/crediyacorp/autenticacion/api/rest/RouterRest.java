package co.crediyacorp.autenticacion.api.rest;

import co.crediyacorp.autenticacion.api.config.UsuarioPath;
import co.crediyacorp.autenticacion.api.dtos.UsuarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final UsuarioPath usuarioPath;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuario/registrar",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = "registrarUsuario",
                            summary = "Registrar nuevo usuario",
                            description = """
                                Se debe poder registrar un nuevo solicitante proporcionando sus datos personales.
                                Validaciones:
                                - nombres, apellidos, correo_electronico y salario_base no pueden ser nulos o vacios.
                                - correo_electronico debe ser unico en el sistema.
                                - correo_electronico debe tener formato valido.
                                - salario_base debe ser numerico entre 0 y 15000000.
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
            ),
            @RouterOperation(
                    path = "/api/v1/usuario/validar",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "validarUsuario",
                            summary = "Validar existencia de usuario",
                            description = """
                                Permite validar si existe un usuario en la base de datos
                                buscando por correo electronico y documento de identidad.
                                """,
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "email",
                                            required = true,
                                            description = "Correo electronico del usuario",
                                            schema = @Schema(type = "string")
                                    ),
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "documentoIdentidad",
                                            required = true,
                                            description = "Número de documento de identidad del usuario",
                                            schema = @Schema(type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Devuelve true si existe, false si no"),
                                    @ApiResponse(responseCode = "400", description = "Parametros invalidos")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuario/login",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = "loginUsuario",
                            summary = "Login de usuario",
                            description = """
                    Permite iniciar sesion en la aplicacion con correo electronico y contrasena.
                    Devuelve un token JWT si las credenciales son correctas.
                    """,
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos de login del usuario",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    example = """
                                        {
                                            "email":"cristianadmin@example.com",
                                            "contrasena" : "porDefecto123"
                                        }
                                        """
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Login exitoso, devuelve token",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(
                                                            example = """
                                                {
                                                    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjcmlzdGlhbmFkbWluQGV4YW1wbGUuY29tIiwicm9sZSI6ImQ2ZjM3OGE0LThiMWUtNGYyYS05YzNkLTEyMzQ1Njc4OWFiYyIsImlhdCI6MTc1NjYwMDk1MiwiZXhwIjoxNzU2NjA0NTUyfQ.uA_Cn9rKBY26yEH538T4rNTLBxMb4nLquBG-yvVMrSA",
                                                    "type": "Bearer"
                                                }
                                                """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(responseCode = "401", description = "Credenciales invalidas"),
                                    @ApiResponse(responseCode = "400", description = "Parametros invalidos")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuario/salario",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = "obtenerSalariosBasePorEmails",
                            summary = "Obtener salarios base por lista de correos",
                            description = """
                    Recibe una lista de correos electronicos en el cuerpo de la peticion
                    y devuelve una lista de salarios base (BigDecimal) en el mismo orden.
                    """,
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    description = "Lista de correos electronicos",
                                    content = @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(type = "string", example = "usuario@example.com"))
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de salarios base devuelta en el mismo orden que los correos enviados",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    array = @ArraySchema(schema = @Schema(type = "number", format = "bigdecimal", example = "4500000.00"))
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Lista de correos invalida"),
                                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                            }
                    )
            )
            , @RouterOperation(
            path = "/api/v1/salario",
            method = RequestMethod.GET,
            operation = @Operation(
                    operationId = "obtenerSalarioBasePorEmail",
                    summary = "Obtener salario base de usuario",
                    description = "Permite obtener el salario base de un usuario a partir de su correo electrónico.",
                    parameters = {
                            @Parameter(
                                    name = "email",
                                    description = "Correo electrónico del usuario",
                                    required = true,
                                    example = "cristianmanuel2304@gmail.com"
                            )
                    },
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Salario base obtenido correctamente",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    example = "2500.00"
                                            )
                                    )
                            ),
                            @ApiResponse(responseCode = "400", description = "El parámetro 'email' es obligatorio"),
                            @ApiResponse(responseCode = "404", description = "El usuario no existe en la base de datos")
                    }
            )
    )



    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(usuarioPath.getRegistrar()), handler::listenRegistrarUsuario)
                .andRoute(GET(usuarioPath.getValidar()), handler::listenValidarUsuario)
                .andRoute(POST(usuarioPath.getLogin()), handler::listenLoginUsuario)
                .andRoute(POST(usuarioPath.getSalario()), handler::listenObtenerSalariosBasePorEmails)
                .andRoute(GET(usuarioPath.getSalario()), handler::listenObtenerSalarioBasePorEmail);
    }
}
