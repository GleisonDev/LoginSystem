package com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "O nome de usuário é obrigatório.")
        String nomeUsuario,

        @NotBlank(message = "A senha é obrigatória.")
        String senha
) {
}
