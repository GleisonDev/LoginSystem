package com.sireostech.vstore.gestao.infrastructure.api.perfil.mapper;

import com.sireostech.vstore.gestao.domain.gateway.PerfilGateway;
import com.sireostech.vstore.gestao.domain.model.Perfil;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Mapper(
        componentModel = ComponentModel.SPRING
)
public abstract class PerfilMapper {

    @Autowired
    protected PerfilGateway perfilGateway;

    public Perfil map(String perfilNome) {
        if (perfilNome == null) {
            return null;
        }

        return perfilGateway.findByNome(perfilNome.toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Perfil de usuário inválido ou inexistente: " + perfilNome)
                );
    }

    public String map(Perfil perfil) {
        return perfil != null ? perfil.getNome() : null;
    }
}