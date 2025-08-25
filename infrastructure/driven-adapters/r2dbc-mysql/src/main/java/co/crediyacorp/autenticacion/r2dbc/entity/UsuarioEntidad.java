package co.crediyacorp.autenticacion.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "usuario")
public class UsuarioEntidad implements Persistable<String> {

    @Id
    @Column("id_usuario")
    @Builder.Default
    private String idUsuario=UUID.randomUUID().toString();

    @Column("nombres")
    private String nombres;

    @Column("apellidos")
    private String apellidos;

    @Column("email")
    private String email;

    @Column("documento_identidad")
    private String documentoIdentidad;

    @Column("telefono")
    private String telefono;

    @Column("direccion")
    private String direccion;

    @Column("salario_base")
    private BigDecimal salarioBase;

    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column("contrasena")
    private String contrasena;

    @Column("rol_id")
    private String rolId;

    @Transient
    private boolean isNew = true; // por defecto, nuevo

    @Override
    public String getId() {
        return this.idUsuario;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    public void markAsPersisted() {
        this.isNew = false;
    }

}
