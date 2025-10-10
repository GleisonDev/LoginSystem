package com.sireostech.vstore.gestao.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuarios")
@Data
@RequiredArgsConstructor
@NoArgsConstructor // Necessário para a JPA
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_usuario", length = 100, nullable = false, unique = true)
    @NonNull
    private String nomeUsuario;

    @Column(name = "senha_hash", length = 255, nullable = false)
    @NonNull
    private String senhaHash;

    @Column(name = "nome_completo", length = 255)
    private String nomeCompleto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "perfil_id", nullable = false)
    @NonNull
    private Perfil perfil;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.perfil.getPermissoes().stream()
                .map(permissao -> new SimpleGrantedAuthority(permissao.getNome()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.senhaHash;
    }

    @Override
    public String getUsername() {
        return this.nomeUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.ativo != null && this.ativo;
    }

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }
}
