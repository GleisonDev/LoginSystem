package com.sireostech.vstore.gestao.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "VStore API - Gestão",
                version = "1.0",
                description = "API responsável pela autenticação, controle e gestão de usuários da VStore.",
                contact = @Contact(name = "Sireos Tech", email = "contato@sireostech.com")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor Local"),
                @Server(url = "https://api.suaempresa.com", description = "Servidor Produção")
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        },
        tags = {
                @Tag(name = "Autenticação", description = "Endpoints de login, logout e renovação de tokens"),
                @Tag(name = "Usuários", description = "Endpoints de criação, atualização e listagem de usuários"),
                @Tag(name = "Produtos", description = "Endpoints de criação, atualização e listagem de produtos"),
                @Tag(name = "Vendas", description = "Endpoints relacionados a pedidos e vendas")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

}
