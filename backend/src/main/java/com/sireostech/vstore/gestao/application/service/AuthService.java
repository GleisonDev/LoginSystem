package com.sireostech.vstore.gestao.application.service;

import com.sireostech.vstore.gestao.application.contracts.AuthContracts;
import com.sireostech.vstore.gestao.domain.gateway.UsuarioGateway;
import com.sireostech.vstore.gestao.domain.model.IdentifiablePrincipal;
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
    private static final String REFRESH_TOKEN_USER_PREFIX = "refresh_token:user_";
    private static final Long ACCESS_TOKEN_EXPIRATION_SECONDS = 7200L;

    private LoginResponseDTO generateAndStoreTokens(IdentifiablePrincipal principal) {

        String accessToken = tokenProvider.generateToken(principal);

        String refreshToken = UUID.randomUUID().toString();

        String userIdKey = String.valueOf(principal.getId());

        redisTemplate.opsForValue().set(refreshToken, userIdKey, REFRESH_TOKEN_EXPIRATION);

        redisTemplate.opsForValue().set(REFRESH_TOKEN_USER_PREFIX + userIdKey, refreshToken, REFRESH_TOKEN_EXPIRATION);


        return new LoginResponseDTO(
                accessToken,
                refreshToken,
                "Bearer",
                ACCESS_TOKEN_EXPIRATION_SECONDS
        );
    }

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.nomeUsuario(), dto.senha())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Usuario authenticatedUser = (Usuario) authentication.getPrincipal();

        return generateAndStoreTokens(authenticatedUser);
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
                ACCESS_TOKEN_EXPIRATION_SECONDS
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
            Duration remainingTTL = tokenProvider.getTokenExpirationRemaining(token);
            blacklistProvider.blacklistToken(token, remainingTTL);

            String userId = tokenProvider.getUserIdFromToken(token);
            String userIdKey = String.valueOf(userId);

            String refreshToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_USER_PREFIX + userIdKey);

            if (refreshToken != null) {
                redisTemplate.delete(refreshToken);
            }

            redisTemplate.delete(REFRESH_TOKEN_USER_PREFIX + userIdKey);

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