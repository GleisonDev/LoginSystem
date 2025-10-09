package com.sireostech.vstore.gestao.infrastructure.api.security.jwt;

import com.sireostech.vstore.gestao.infrastructure.api.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    // Injetamos a implementação do UserDetailsService para carregar o usuário
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recoverToken(request);

        if (token != null) {
            try {
                // 1. Valida o token e extrai o ID do usuário
                String userId = tokenProvider.validateTokenAndGetUserId(token);

                // 2. Carrega o usuário autenticado a partir do ID
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                // 3. Cria a autenticação (UserDetails, Credenciais=null, Authorities)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // 4. Define o contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Se falhar (token expirado/inválido), limpa o contexto
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Retorna apenas o token JWT
        }
        return null;
    }
}