package com.sireostech.vstore.gestao.application.contracts;

import com.sireostech.vstore.gestao.domain.model.Perfil;
import java.util.List;
import java.util.Optional;

public interface PerfilContracts {
    Perfil createPerfil(Perfil perfil);
    Optional<Perfil> findByNome(String nome);
    List<Perfil> findAll();
}