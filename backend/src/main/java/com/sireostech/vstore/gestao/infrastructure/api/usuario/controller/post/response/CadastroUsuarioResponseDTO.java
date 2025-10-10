package com.sireostech.vstore.gestao.infrastructure.api.usuario.controller.post.response;

import com.sireostech.vstore.gestao.domain.model.Usuario;
import java.time.LocalDateTime;

public record CadastroUsuarioResponseDTO(
        Long id,
        String nomeUsuario,
        String nomeCompleto,
        String perfil,
        LocalDateTime dataCriacao
) {

    public static CadastroUsuarioResponseDTO fromEntity(Usuario usuario) {
        String nomePerfil = null;
        if (usuario.getPerfil() != null) {
            nomePerfil = usuario.getPerfil().getNome();
        }

        return new CadastroUsuarioResponseDTO(
                usuario.getId(),
                usuario.getNomeUsuario(),
                usuario.getNomeCompleto(),
                nomePerfil,
                usuario.getDataCriacao()
        );
    }
}
