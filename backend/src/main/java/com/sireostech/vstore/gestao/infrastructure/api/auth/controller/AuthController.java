package com.sireostech.vstore.gestao.infrastructure.api.auth.controller;

import com.sireostech.vstore.gestao.application.contracts.AuthContracts;
import com.sireostech.vstore.gestao.infrastructure.api.auth.AuthApi;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.request.LoginRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.request.RefreshTokenRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.LoginResponseDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.LogoutResponseDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.RefreshTokenResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthContracts contracts;

    @Override
    public ResponseEntity<LoginResponseDTO> authenticate(LoginRequestDTO request) {
        LoginResponseDTO response = contracts.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<LogoutResponseDTO> logout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authorizationHeader.substring(7).trim();

        contracts.logout(token);

        return ResponseEntity.ok(new LogoutResponseDTO("Usuário deslogado com sucesso."));
    }

    @Override
    public ResponseEntity<RefreshTokenResponseDTO> refresh(RefreshTokenRequestDTO request) {
        RefreshTokenResponseDTO response = contracts.refreshToken(request);
        return ResponseEntity.ok(response);
    }

}
