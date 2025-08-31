package co.crediyacorp.autenticacion.api.mappers;



import co.crediyacorp.autenticacion.api.dtos.UsuarioDto;
import co.crediyacorp.autenticacion.api.dtos.UsuarioResponseDto;
import co.crediyacorp.autenticacion.model.rol.RolEnum;
import co.crediyacorp.autenticacion.model.rol.gateways.RolRepository;
import co.crediyacorp.autenticacion.model.usuario.Usuario;

import co.crediyacorp.autenticacion.model.usuario.gateways.ClaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;



@Component
@RequiredArgsConstructor
public class UsuarioMapper {


    private final RolRepository rolRepository;
    private final ClaveService claveService;


    public Mono<Usuario> toDomain(UsuarioDto usuarioDto) {
        return rolRepository.obtenerIdRolPorNombre(RolEnum.valueOf(usuarioDto.rol()))
                .zipWith(claveService.encriptarClave("porDefecto123"))
                .map(tupla -> Usuario.builder()
                        .nombres(usuarioDto.nombres())
                        .apellidos(usuarioDto.apellidos())
                        .documentoIdentidad(usuarioDto.documentoIdentidad())
                        .email(usuarioDto.email())
                        .telefono(usuarioDto.telefono())
                        .rolId(tupla.getT1()).
                        contrasena(tupla.getT2())
                        .fechaNacimiento(usuarioDto.fechaNacimiento())
                        .direccion(usuarioDto.direccion())
                        .salarioBase(usuarioDto.salarioBase())
                        .build());


    }

    public Mono<UsuarioResponseDto> fromEntityToUsuarioResponseDto(Usuario usuario) {
        return Mono.just(new UsuarioResponseDto(
                usuario.getIdUsuario(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getEmail(),
                usuario.getDocumentoIdentidad(),
                usuario.getTelefono(),
                usuario.getDireccion(),
                usuario.getSalarioBase(),
                usuario.getFechaNacimiento(),
                usuario.getRolId()
        ));
    }


}
