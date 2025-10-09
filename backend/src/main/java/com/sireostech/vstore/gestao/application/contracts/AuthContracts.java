package com.sireostech.vstore.gestao.application.contracts;

import com.sireostech.vstore.gestao.domain.model.Usuario;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.request.LoginRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.LoginResponseDTO;

public interface AuthContracts {

    LoginResponseDTO authenticate(LoginRequestDTO dto);

    // Simplificado para o escopo atual, mas você pode expandir
    Usuario getAuthenticatedUser();

    // Outros métodos como logout() ou refreshAccessToken() podem ser adicionados depois.
}
