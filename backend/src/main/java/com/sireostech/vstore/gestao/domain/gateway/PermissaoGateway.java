package com.sireostech.vstore.gestao.domain.gateway;

import com.sireostech.vstore.gestao.domain.model.Permissao;
import java.util.Optional;

public interface PermissaoGateway {
    Permissao save(Permissao permissao);
    Optional<Permissao> findByNome(String nome);
}