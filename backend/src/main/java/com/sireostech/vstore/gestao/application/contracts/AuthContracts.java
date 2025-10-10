package com.sireostech.vstore.gestao.application.contracts;

import com.sireostech.vstore.gestao.domain.model.Usuario;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.request.LoginRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.request.RefreshTokenRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.LoginResponseDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.RefreshTokenResponseDTO;

public interface AuthContracts {

    LoginResponseDTO authenticate(LoginRequestDTO dto);

    Usuario getAuthenticatedUser();

    void logout(String token);

    RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO request);

}
