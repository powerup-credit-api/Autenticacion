package co.crediyacorp.autenticacion.api.mappers;


import co.crediyacorp.autenticacion.api.dtos.UsuarioDto;
import co.crediyacorp.autenticacion.model.rol.RolEnum;
import co.crediyacorp.autenticacion.model.rol.gateways.RolRepository;
import co.crediyacorp.autenticacion.model.usuario.Usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;



@Component
@RequiredArgsConstructor
public class UsuarioMapper {


    private final RolRepository rolRepository;

    public Mono<Usuario> toDomain(UsuarioDto usuarioDto) {
        return rolRepository.obtenerIdRolPorNombre(RolEnum.valueOf(usuarioDto.rol())).map(rolId ->
                Usuario.builder()
                .nombres(usuarioDto.nombres())
                .apellidos(usuarioDto.apellidos())
                .documentoIdentidad(usuarioDto.documentoIdentidad())
                .email(usuarioDto.email())
                .telefono(usuarioDto.telefono())
                .rolId(rolId)
                .fechaNacimiento(usuarioDto.fechaNacimiento())
                .direccion(usuarioDto.direccion())
                .salarioBase(usuarioDto.salarioBase())
                .build());


    }




}
