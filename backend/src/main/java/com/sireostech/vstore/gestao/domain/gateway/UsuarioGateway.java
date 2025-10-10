package com.sireostech.vstore.gestao.domain.gateway;

import com.sireostech.vstore.gestao.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioGateway {

    Usuario save(Usuario usuario);

    Optional<Usuario> findByNomeUsuario(String nomeUsuario);

    Optional<Usuario> findById(Long id);

    void desanexar(Usuario usuario);
}