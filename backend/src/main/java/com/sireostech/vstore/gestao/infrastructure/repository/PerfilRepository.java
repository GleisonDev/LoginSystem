package com.sireostech.vstore.gestao.infrastructure.repository;

import com.sireostech.vstore.gestao.domain.gateway.PerfilGateway;
import com.sireostech.vstore.gestao.domain.model.Perfil;
import java.util.Optional;

public interface PerfilRepository extends CustomJpaRepository<Perfil, Long>, PerfilGateway {

    @Override
    Optional<Perfil> findByNome(String nome);
}