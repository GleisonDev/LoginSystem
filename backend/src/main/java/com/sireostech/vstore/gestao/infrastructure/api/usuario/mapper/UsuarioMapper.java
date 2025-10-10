package com.sireostech.vstore.gestao.infrastructure.api.usuario.mapper;

import com.sireostech.vstore.gestao.domain.enums.PerfilUsuario;
import com.sireostech.vstore.gestao.domain.model.Usuario;
import com.sireostech.vstore.gestao.infrastructure.api.usuario.controller.post.request.CadastroUsuarioRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.usuario.controller.post.response.CadastroUsuarioResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(source = "senha", target = "senhaHash")
    @Mapping(source = "perfil", target = "perfil", qualifiedByName = "mapPerfil")
    Usuario toEntity(CadastroUsuarioRequestDTO request);

    @Mapping(source = "perfil", target = "perfil", qualifiedByName = "mapPerfilToString")
    CadastroUsuarioResponseDTO toResponseDto(Usuario usuario);

    @Named("mapPerfil")
    default PerfilUsuario mapPerfil(String perfilString) {
        if (perfilString == null) {
            return null;
        }
        try {
            return PerfilUsuario.valueOf(perfilString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Perfil de usuário inválido: " + perfilString);
        }
    }

    @Named("mapPerfilToString")
    default String mapPerfilToString(PerfilUsuario perfil) {
        return perfil != null ? perfil.name() : null;
    }
}