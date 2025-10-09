package com.sireostech.vstore.gestao.application.service;

import com.sireostech.vstore.gestao.application.contracts.AuthContracts;
import com.sireostech.vstore.gestao.domain.model.Usuario;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.request.LoginRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.LoginResponseDTO;
import com.sireostech.vstore.gestao.infrastructure.api.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthContracts {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO dto) {

        // 1. Tenta autenticar (dispara o UserDetailsService)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.nomeUsuario(), dto.senha())
        );

        // 2. Opcional, mas recomendado: salva a autenticação no contexto
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Obtém o usuário (Principal) e gera o token
        Usuario authenticatedUser = (Usuario) authentication.getPrincipal();

        String token = tokenProvider.generateToken(authenticatedUser);

        // Retornamos o DTO de resposta
        return new LoginResponseDTO(
                token,
                "Bearer",
                7200L // 2 horas (tempo que definimos no JwtTokenProvider, em segundos)
        );
    }

    @Override
    public Usuario getAuthenticatedUser() {
        // Recupera o objeto de usuário do contexto de segurança
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return (Usuario) authentication.getPrincipal();
        }
        // Em um cenário real, você lançaria uma exceção aqui se o usuário não estivesse logado.
        return null;
    }

    // Implementações vazias de refresh/logout que você pode adicionar depois
    // @Override
    // public void logout() { ... }
    // @Override
    // public LoginResponseDTO refreshAccessToken(String refreshTokenStr, String tenant) { ... }
}