CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,

    nome_usuario VARCHAR(100) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,

    nome_completo VARCHAR(255) NOT NULL,

    perfil VARCHAR(50) NOT NULL, -- Ex: 'ADMINISTRADOR', 'VENDEDOR'

    ativo BOOLEAN NOT NULL DEFAULT TRUE, -- Flag para desativar o usuário

    data_criacao TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_usuario_nome_usuario ON usuarios (nome_usuario);