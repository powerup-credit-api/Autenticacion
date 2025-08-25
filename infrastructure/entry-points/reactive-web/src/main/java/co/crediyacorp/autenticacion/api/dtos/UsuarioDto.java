package co.crediyacorp.autenticacion.api.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;


public record UsuarioDto(
    String nombres,

    String apellidos,

    String email,

    String documentoIdentidad,

    String telefono,

    String direccion,

    BigDecimal salarioBase,

    LocalDate fechaNacimiento,

    String rol
){ }
