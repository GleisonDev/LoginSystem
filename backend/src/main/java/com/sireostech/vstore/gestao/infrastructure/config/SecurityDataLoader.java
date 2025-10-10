package com.sireostech.vstore.gestao.infrastructure.config;

import com.sireostech.vstore.gestao.domain.gateway.PerfilGateway;
import com.sireostech.vstore.gestao.domain.gateway.PermissaoGateway;
import com.sireostech.vstore.gestao.domain.gateway.UsuarioGateway; // Usa o Gateway (Porta)
import com.sireostech.vstore.gestao.domain.model.Perfil;
import com.sireostech.vstore.gestao.domain.model.Permissao;
import com.sireostech.vstore.gestao.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityDataLoader implements CommandLineRunner {

    private final PerfilGateway perfilGateway;
    private final PermissaoGateway permissaoGateway;
    private final UsuarioGateway usuarioGateway;
    private final PasswordEncoder passwordEncoder;

    private static final List<String> ALL_PERMISSIONS = Arrays.asList(
            "clientes_visualizar", "clientes_gerenciar",
            "vendas_registrar", "vendas_visualizar",
            "estoque_visualizar", "estoque_gerenciar",
            "usuarios_gerenciar", "perfis_gerenciar",
            "relatorios_vendas_visualizar",
            "dados_pessoais_editar"
    );

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando carregamento de dados de segurança (RBAC) para 4 Perfis...");

        Set<Permissao> todasPermissoes = createOrFindPermissoes(ALL_PERMISSIONS);

        Set<Permissao> permissoesProprietario = new HashSet<>(todasPermissoes);
        Perfil proprietario = createOrUpdatePerfil("PROPRIETARIO", permissoesProprietario);

        Set<Permissao> permissoesGerente = todasPermissoes.stream()
                .filter(p -> !p.getNome().contains("usuarios_gerenciar") &&
                        !p.getNome().contains("perfis_gerenciar"))
                .collect(Collectors.toSet());
        Perfil gerente = createOrUpdatePerfil("GERENTE", permissoesGerente);

        Set<Permissao> permissoesVendedor = todasPermissoes.stream()
                .filter(p -> p.getNome().startsWith("clientes_") ||
                        p.getNome().startsWith("vendas_registrar") ||
                        p.getNome().startsWith("estoque_visualizar") ||
                        p.getNome().startsWith("dados_pessoais_editar"))
                .collect(Collectors.toSet());
        Perfil vendedor = createOrUpdatePerfil("VENDEDOR", permissoesVendedor);

        Set<Permissao> permissoesCliente = todasPermissoes.stream()
                .filter(p -> p.getNome().equals("dados_pessoais_editar"))
                .collect(Collectors.toSet());
        Perfil cliente = createOrUpdatePerfil("CLIENTE", permissoesCliente);

        createInitialUser("proprietario", "senha123", proprietario, "Usuário Proprietário Inicial");
        createInitialUser("vendedor1", "venda123", vendedor, "Usuário Vendedor Exemplo");

        log.info("Carregamento de dados de segurança (RBAC) finalizado com sucesso.");
    }

    private Perfil createOrUpdatePerfil(String nome, Set<Permissao> permissoes) {
        Optional<Perfil> perfilOpt = perfilGateway.findByNome(nome);
        Perfil perfil;

        if (perfilOpt.isEmpty()) {
            perfil = new Perfil(nome);
            perfil.setPermissoes(permissoes);
            perfilGateway.save(perfil);
            log.info("Perfil {} criado com {} permissões.", nome, permissoes.size());
        } else {
            perfil = perfilOpt.get();
            perfil.setPermissoes(permissoes);
            perfilGateway.save(perfil);
            log.info("Perfil {} atualizado com {} permissões.", nome, permissoes.size());
        }
        return perfil;
    }

    private void createInitialUser(String nomeUsuario, String senha, Perfil perfil, String nomeCompleto) {
        Optional<Usuario> userOpt = usuarioGateway.findByNomeUsuario(nomeUsuario);

        if (userOpt.isEmpty()) {
            Usuario user = new Usuario(
                    nomeUsuario,
                    passwordEncoder.encode(senha),
                    perfil
            );
            user.setNomeCompleto(nomeCompleto);
            usuarioGateway.save(user);
            log.info("Usuário '{}' criado com perfil '{}'. Senha: {}", nomeUsuario, perfil.getNome(), senha);
        } else {
            log.info("Usuário '{}' já existe. Nenhuma alteração feita.", nomeUsuario);
        }
    }

    private Set<Permissao> createOrFindPermissoes(List<String> nomes) {
        Set<Permissao> permissoes = new HashSet<>();
        for (String nome : nomes) {
            Optional<Permissao> permissaoOpt = permissaoGateway.findByNome(nome);
            if (permissaoOpt.isEmpty()) {
                Permissao novaPermissao = new Permissao(nome);
                permissoes.add(permissaoGateway.save(novaPermissao));
            } else {
                permissoes.add(permissaoOpt.get());
            }
        }
        return permissoes;
    }
}