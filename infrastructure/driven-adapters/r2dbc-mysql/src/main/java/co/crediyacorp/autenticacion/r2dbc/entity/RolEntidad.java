package co.crediyacorp.autenticacion.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;



@Table(name = "rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolEntidad {

    @Id
    private String idRol;

    @Column("nombre")
    private RolEnum rol;

    @Column("descripcion")
    private String descripcion;
}
