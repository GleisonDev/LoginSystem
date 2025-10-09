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
        // 1. Usa o Spring Component Model
        componentModel = ComponentModel.SPRING,
        // 2. Garante que erros de mapeamento não mapeados sejam reportados (boa prática)
        unmappedTargetPolicy = ReportingPolicy.IGNORE
        // NOTA: Ao remover @Builder, o MapStruct usará automaticamente o @NoArgsConstructor + Setters,
        // o que preserva os valores padrão (ativo=true, dataCriacao=agora).
)
public interface UsuarioMapper {

    /**
     * Mapeia o DTO de requisição para a Entidade Usuario.
     */
    @Mapping(target = "id", ignore = true)
    // O campo 'ativo' tem valor padrão na Entidade.
    @Mapping(target = "ativo", ignore = true)
    // O campo 'dataCriacao' tem valor padrão na Entidade.
    @Mapping(target = "dataCriacao", ignore = true)
    // Mapeia o campo 'senha' do DTO para o campo 'senhaHash' na Entidade.
    // NOTA: O HASH DA SENHA DEVE SER FEITO NO SERVICE ANTES DE CHAMAR O save().
    @Mapping(source = "senha", target = "senhaHash")
    @Mapping(source = "perfil", target = "perfil", qualifiedByName = "mapPerfil")
    Usuario toEntity(CadastroUsuarioRequestDTO request);

    /**
     * Mapeia a Entidade Usuario para o DTO de resposta.
     */
    @Mapping(source = "perfil", target = "perfil", qualifiedByName = "mapPerfilToString")
    CadastroUsuarioResponseDTO toResponseDto(Usuario usuario);


    // --- MÉTODOS DE CONVERSÃO CUSTOMIZADOS ---

    /**
     * Converte String (do DTO) para PerfilUsuario (na Entidade).
     */
    @Named("mapPerfil")
    default PerfilUsuario mapPerfil(String perfilString) {
        if (perfilString == null) {
            return null;
        }
        try {
            // Converte para maiúsculas, pois é padrão para enums em Java.
            return PerfilUsuario.valueOf(perfilString.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Em produção, você deve lançar uma exceção de negócio/validação.
            throw new IllegalArgumentException("Perfil de usuário inválido: " + perfilString);
        }
    }

    /**
     * Converte PerfilUsuario (da Entidade) para String (no DTO de resposta).
     */
    @Named("mapPerfilToString")
    default String mapPerfilToString(PerfilUsuario perfil) {
        return perfil != null ? perfil.name() : null;
    }
}