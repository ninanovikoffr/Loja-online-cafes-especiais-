CREATE TABLE pedidos (
    id_pedido BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_endereco BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'aguardando',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_pedido_endereco FOREIGN KEY (id_endereco) REFERENCES enderecos(id_endereco) ON DELETE CASCADE
);
