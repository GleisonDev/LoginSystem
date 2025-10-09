package com.sireostech.vstore.gestao.infrastructure.api.auth.controller;

import com.sireostech.vstore.gestao.application.contracts.AuthContracts;
import com.sireostech.vstore.gestao.infrastructure.api.auth.AuthApi;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.request.LoginRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor// Deve ser uma classe RestController
public class AuthController implements AuthApi { // Implementa a Interface de API

    private final AuthContracts authService; // A interface do seu serviço
    /**
     * Implementação do endpoint de login definido na AuthApi.
     * As anotações de rota (@PostMapping, @RequestMapping) e Swagger já estão na interface.
     */
    @Override
    public ResponseEntity<LoginResponseDTO> authenticate(LoginRequestDTO request) {

        // Chama a lógica de negócio (autenticação e geração de token)
        LoginResponseDTO response = authService.authenticate(request);

        return ResponseEntity.ok(response);
    }
}