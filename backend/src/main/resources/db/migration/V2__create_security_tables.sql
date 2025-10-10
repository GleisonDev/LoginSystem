CREATE TABLE permissoes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE perfis (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE perfil_permissoes (
    perfil_id BIGINT NOT NULL,
    permissoes_id BIGINT NOT NULL,

    CONSTRAINT fk_perfil_permissoes_perfil FOREIGN KEY (perfil_id) REFERENCES perfis (id),
    CONSTRAINT fk_perfil_permissoes_permissao FOREIGN KEY (permissoes_id) REFERENCES permissoes (id),

    PRIMARY KEY (perfil_id, permissoes_id)
);

ALTER TABLE usuarios DROP COLUMN perfil;

ALTER TABLE usuarios ADD COLUMN perfil_id BIGINT NOT NULL;

ALTER TABLE usuarios ADD CONSTRAINT fk_usuarios_perfil
    FOREIGN KEY (perfil_id) REFERENCES perfis (id);