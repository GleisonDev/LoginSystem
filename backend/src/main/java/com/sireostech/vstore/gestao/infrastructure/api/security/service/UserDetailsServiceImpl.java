package com.sireostech.vstore.gestao.infrastructure.api.security.service;

import com.sireostech.vstore.gestao.domain.gateway.UsuarioGateway;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioGateway usuarioGateway;

    public UserDetailsServiceImpl(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    /**
     * Implementa a lógica de busca do usuário.
     * * @param username Pode ser o ID do usuário (vindo do token JWT) ou o nome de usuário (vindo do login).
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Tenta buscar por ID (usado pelo JWT Filter, onde o Subject é o ID)
        try {
            Long userId = Long.parseLong(username);

            // SE A BUSCA POR ID FOR BEM-SUCEDIDA, RETORNA O USUÁRIO E SAI DO MÉTODO
            return usuarioGateway.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com ID: " + userId));

        } catch (NumberFormatException e) {
            // Se o 'username' não for um número, a execução continua para a busca por nome de usuário.
            // Isso acontece quando o endpoint de LOGIN chama este método.
        }

        // 2. Busca por Nome de Usuário (usado pela autenticação padrão do Spring/Login)
        return usuarioGateway.findByNomeUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}