package co.crediyacorp.autenticacion.api.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "routes.paths")
public class UsuarioPath {
    private String registrar;
    private String validar;
}
