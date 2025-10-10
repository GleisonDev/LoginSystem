package com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response;

public record LoginResponseDTO(
        String token,
        String refreshToken,
        String type,
        Long expiresIn
) {
}
