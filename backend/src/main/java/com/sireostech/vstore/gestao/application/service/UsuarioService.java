package com.sireostech.vstore.gestao.application.service;

import com.sireostech.vstore.gestao.application.contracts.UsuarioContracts;
import com.sireostech.vstore.gestao.domain.gateway.UsuarioGateway;
import com.sireostech.vstore.gestao.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioContracts {

    private final UsuarioGateway gateway;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario cadastrarNovoUsuario(Usuario usuario) {

        if (gateway.findByNomeUsuario(usuario.getNomeUsuario()).isPresent()) {
            throw new RuntimeException("Nome de usuário já existe: " + usuario.getNomeUsuario());
        }

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenhaHash());
        usuario.setSenhaHash(senhaCriptografada);

        usuario.setDataCriacao(LocalDateTime.now());

        return gateway.save(usuario);
    }

    @Override
    public Usuario buscarUsuarioPorNome(String nomeUsuario) {
        return gateway.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + nomeUsuario));
    }
}