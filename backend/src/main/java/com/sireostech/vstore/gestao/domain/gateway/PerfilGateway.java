package com.sireostech.vstore.gestao.domain.gateway;

import com.sireostech.vstore.gestao.domain.model.Perfil;

import java.util.List;
import java.util.Optional;

public interface PerfilGateway {
    Perfil save(Perfil perfil);
    Optional<Perfil> findByNome(String nome);
    List<Perfil> findAll();
}