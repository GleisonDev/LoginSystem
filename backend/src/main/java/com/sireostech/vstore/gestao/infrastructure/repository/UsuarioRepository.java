package com.sireostech.vstore.gestao.infrastructure.repository;

import com.sireostech.vstore.gestao.domain.gateway.UsuarioGateway;
import com.sireostech.vstore.gestao.domain.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long>, UsuarioGateway {

    @Override
    Optional<Usuario> findByNomeUsuario(String nomeUsuario);

    @Override
    default void desanexar(Usuario usuario) {
        detach(usuario);
    }
}