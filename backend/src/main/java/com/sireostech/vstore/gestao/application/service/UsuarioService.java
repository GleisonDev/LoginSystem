package com.sireostech.vstore.gestao.application.service;

import com.sireostech.vstore.gestao.application.contracts.UsuarioContracts;
import com.sireostech.vstore.gestao.domain.gateway.UsuarioGateway;
import com.sireostech.vstore.gestao.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service // Anotação Spring para indicar que é um componente de serviço
@RequiredArgsConstructor // Lombok para injetar dependências via construtor
public class UsuarioService implements UsuarioContracts {

    // ----------------------------------------------------
    // Dependências Injetadas
    // ----------------------------------------------------
    private final UsuarioGateway usuarioGateway; // Acesso ao banco de dados via porta do domínio
    private final PasswordEncoder passwordEncoder; // O codificador de senhas
    // ----------------------------------------------------

    @Override
    public Usuario cadastrarNovoUsuario(Usuario usuario) {

        // 1. Validação Simples (Exemplo)
        if (usuarioGateway.findByNomeUsuario(usuario.getNomeUsuario()).isPresent()) {
            // Em uma implementação real, lançaríamos uma exceção 409 Conflict
            throw new RuntimeException("Nome de usuário já existe: " + usuario.getNomeUsuario());
        }

        // 2. CRIPTOGRAFIA DA SENHA (Lógica de Negócio de Segurança)
        // O valor em 'senhaHash' no objeto 'usuario' neste momento é a senha em texto puro (do DTO).
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenhaHash());
        usuario.setSenhaHash(senhaCriptografada);

        // 3. Define a data de criação
        usuario.setDataCriacao(LocalDateTime.now());

        // 4. Persiste o usuário (o Gateway chama o Repository na Infra)
        return usuarioGateway.save(usuario);
    }

    @Override
    public Usuario buscarUsuarioPorNome(String nomeUsuario) {
        // Implementação necessária para o Spring Security carregar os dados do usuário no login
        return usuarioGateway.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + nomeUsuario));
    }
}