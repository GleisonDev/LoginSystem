package com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response;

public record LoginResponseDTO(
        String token,
        String type, // Ex: "Bearer"
        Long expiresIn // Tempo de expiração em segundos/milissegundos
) {
}
