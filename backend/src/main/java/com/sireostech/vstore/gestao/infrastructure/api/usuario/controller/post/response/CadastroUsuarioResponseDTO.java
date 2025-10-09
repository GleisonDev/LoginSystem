package com.sireostech.vstore.gestao.infrastructure.api.usuario.controller.post.response;

import java.time.LocalDateTime;

public record CadastroUsuarioResponseDTO(
        Long id,
        String nomeUsuario,
        String nomeCompleto,
        String perfil,
        LocalDateTime dataCriacao
) {}