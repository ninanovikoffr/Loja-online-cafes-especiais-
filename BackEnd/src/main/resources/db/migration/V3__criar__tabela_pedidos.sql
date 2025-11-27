CREATE TABLE pedidos (
    id_pedido BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT,
    id_endereco BIGINT,
    status VARCHAR(20),
    created_at TIMESTAMP,
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_pedido_endereco FOREIGN KEY (id_endereco) REFERENCES enderecos(id_endereco) ON DELETE CASCADE
);
