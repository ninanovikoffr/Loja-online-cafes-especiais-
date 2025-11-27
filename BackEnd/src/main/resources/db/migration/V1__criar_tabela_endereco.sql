CREATE TABLE enderecos (
    id_endereco BIGSERIAL PRIMARY KEY,
    cep VARCHAR(9),
    rua VARCHAR(150),
    numero VARCHAR(10),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    is_padrao BOOLEAN,
    created_at TIMESTAMP,
    id_usuario BIGINT,
    CONSTRAINT fk_endereco_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);
