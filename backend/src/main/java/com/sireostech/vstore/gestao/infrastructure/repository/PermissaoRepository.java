package com.sireostech.vstore.gestao.infrastructure.repository;

import com.sireostech.vstore.gestao.domain.gateway.PermissaoGateway;
import com.sireostech.vstore.gestao.domain.model.Permissao;

import java.util.Optional;

public interface PermissaoRepository extends CustomJpaRepository<Permissao, Long>, PermissaoGateway {

    @Override
    Optional<Permissao> findByNome(String nome);
}