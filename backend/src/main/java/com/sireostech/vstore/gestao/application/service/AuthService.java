package com.sireostech.vstore.gestao.application.service;

import com.sireostech.vstore.gestao.application.contracts.AuthContracts;
import com.sireostech.vstore.gestao.domain.gateway.UsuarioGateway;
import com.sireostech.vstore.gestao.domain.model.Usuario;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.request.LoginRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.request.RefreshTokenRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.LoginResponseDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.RefreshTokenResponseDTO;
import com.sireostech.vstore.gestao.infrastructure.api.security.jwt.JwtBlacklistProvider;
import com.sireostech.vstore.gestao.infrastructure.api.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService implements AuthContracts {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final JwtBlacklistProvider blacklistProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final UsuarioGateway gateway;

    private static final Duration REFRESH_TOKEN_EXPIRATION = Duration.ofHours(8);
    // Chave para mapear o Refresh Token ao ID do usuário no Redis
    private static final String REFRESH_TOKEN_USER_PREFIX = "refresh_token:user_";

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.nomeUsuario(), dto.senha())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Usuario authenticatedUser = (Usuario) authentication.getPrincipal();

        String accessToken = tokenProvider.generateToken(authenticatedUser);

        String refreshToken = UUID.randomUUID().toString();
        String userIdKey = String.valueOf(authenticatedUser.getId());

        // 1. Salva o refresh token como chave, mapeando para o ID do usuário (Usado para 'refresh')
        redisTemplate.opsForValue().set(refreshToken, userIdKey, REFRESH_TOKEN_EXPIRATION);

        // 2. CORREÇÃO CRÍTICA: Salva o ID do usuário como chave, mapeando para o refresh token (Usado para o 'logout')
        // Esta é a chave que você estava consultando: refresh_token:user_1
        redisTemplate.opsForValue().set(REFRESH_TOKEN_USER_PREFIX + userIdKey, refreshToken, REFRESH_TOKEN_EXPIRATION);


        return new LoginResponseDTO(
                accessToken,
                refreshToken,
                "Bearer",
                7200L

        );
    }

    @Override
    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO request) {

        String storedUserId = redisTemplate.opsForValue().get(request.refreshToken());
        if (storedUserId == null) {
            throw new RuntimeException("Refresh token inválido ou expirado");
        }

        Usuario user = loadUserById(Long.parseLong(storedUserId));

        String newAccessToken = tokenProvider.generateToken(user);

        return new RefreshTokenResponseDTO(
                newAccessToken,
                "Bearer",
                7200L
        );
    }

    @Override
    public Usuario getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return (Usuario) authentication.getPrincipal();
        }
        return null;
    }

    @Override
    public void logout(String token) {
        try {
            // 1. Invalida o Access Token (JWT) na blacklist
            Duration remainingTTL = tokenProvider.getTokenExpirationRemaining(token);
            blacklistProvider.blacklistToken(token, remainingTTL);

            // 2. Extrai o ID do usuário do Access Token para encontrar o Refresh Token no Redis
            String userId = tokenProvider.getUserIdFromToken(token);
            String userIdKey = String.valueOf(userId);

            // 3. Deleta o Refresh Token e o seu mapeamento no Redis (Logoff Completo)
            String refreshToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_USER_PREFIX + userIdKey);

            if (refreshToken != null) {
                // Deleta a chave principal do refresh token (o UUID)
                redisTemplate.delete(refreshToken);
            }
            // Deleta o mapeamento do refresh token pelo ID do usuário (a chave que estava faltando)
            redisTemplate.delete(REFRESH_TOKEN_USER_PREFIX + userIdKey);

            // 4. Limpa o contexto de segurança
            SecurityContextHolder.clearContext();

        } catch (Exception e) {
            System.err.println("❌ [AuthService] Erro ao invalidar token no logout: " + e.getMessage());
        }
    }

    private Usuario loadUserById(Long userId) {
        return gateway.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

}