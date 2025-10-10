package com.sireostech.vstore.gestao.domain.model;

import org.springframework.security.core.userdetails.UserDetails;

public interface IdentifiablePrincipal extends UserDetails {
    Long getId();
    Perfil getPerfil();
}