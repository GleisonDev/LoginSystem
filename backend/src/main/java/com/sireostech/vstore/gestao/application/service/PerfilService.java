package com.sireostech.vstore.gestao.application.service;

import com.sireostech.vstore.gestao.application.contracts.PerfilContracts;
import com.sireostech.vstore.gestao.domain.gateway.PerfilGateway;
import com.sireostech.vstore.gestao.domain.model.Perfil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PerfilService implements PerfilContracts {

    private final PerfilGateway gateway;

    @Override
    public Perfil createPerfil(Perfil perfil) {
        // Lógica de negócio: validar unicidade, etc.
        return gateway.save(perfil);
    }

    @Override
    public Optional<Perfil> findByNome(String nome) {
        return gateway.findByNome(nome);
    }

    @Override
    public List<Perfil> findAll() {
        return gateway.findAll();
    }
}