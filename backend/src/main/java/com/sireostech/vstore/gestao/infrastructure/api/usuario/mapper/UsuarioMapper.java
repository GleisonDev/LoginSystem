package com.sireostech.vstore.gestao.infrastructure.api.usuario.mapper;

import com.sireostech.vstore.gestao.domain.model.Usuario;
import com.sireostech.vstore.gestao.infrastructure.api.usuario.controller.post.request.CadastroUsuarioRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.usuario.controller.post.response.CadastroUsuarioResponseDTO;
import com.sireostech.vstore.gestao.infrastructure.api.perfil.mapper.PerfilMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {PerfilMapper.class}
)
public interface UsuarioMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(source = "senha", target = "senhaHash")
    @Mapping(source = "perfil", target = "perfil")
    Usuario toEntity(CadastroUsuarioRequestDTO request);

    @Mapping(source = "perfil", target = "perfil")
    CadastroUsuarioResponseDTO toResponseDto(Usuario usuario);
}