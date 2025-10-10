package com.sireostech.vstore.gestao.infrastructure.api.usuario.controller.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroUsuarioRequestDTO(
        @NotBlank(message = "O nome de usuário é obrigatório.")
        String nomeUsuario,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String senha,

        @NotBlank(message = "O nome completo é obrigatório.")
        String nomeCompleto,

        @NotBlank(message = "O perfil (ADMINISTRADOR/VENDEDOR) é obrigatório.")
        String perfil
) {}