package co.crediyacorp.autenticacion.model.usuario;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;



@Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public class Usuario {


        private String idUsuario;

        private String nombres;

        private String apellidos;

        private String email;

        private String documentoIdentidad;

        private String telefono;

        private String direccion;

        private BigDecimal salarioBase;

        private LocalDate fechaNacimiento;

        @Builder.Default
        private String contrasena="porDefecto123*";

        private String rolId;


    }
