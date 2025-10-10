package com.sireostech.vstore.gestao.infrastructure.api.security.service;

import com.sireostech.vstore.gestao.domain.gateway.UsuarioGateway;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioGateway gateway;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            Long userId = Long.parseLong(username);

            return gateway.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com ID: " + userId));

        } catch (NumberFormatException e) {

        }

        return gateway.findByNomeUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}