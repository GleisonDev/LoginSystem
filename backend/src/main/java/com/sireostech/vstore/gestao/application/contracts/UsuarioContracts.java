package com.sireostech.vstore.gestao.application.contracts;

import com.sireostech.vstore.gestao.domain.model.Usuario;

public interface UsuarioContracts {

    Usuario cadastrarNovoUsuario(Usuario usuario);

    Usuario buscarUsuarioPorNome(String nomeUsuario);
}