package com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response;

public record RefreshTokenResponseDTO(
        String acessToken,
        String tokenType,
        Long expireIn
) {
}
