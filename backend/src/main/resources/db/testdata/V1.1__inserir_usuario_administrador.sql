INSERT INTO usuarios (id, nome_usuario, senha_hash, nome_completo, perfil, ativo, data_criacao)
VALUES (
    1,
    'admin',
    '$2a$10$TFfZwixmf7lkwwJH3Ul8neeJ0t9E1MYyG7Z0mIS.Yrc6sm16QwLCm', -- Lembre-se que senhas devem ser hasheadas
    'Administrador Global',
    'ADMINISTRADOR',
    TRUE,
    NOW()
);
SELECT setval('usuarios_id_seq', (SELECT MAX(id) FROM usuarios));
