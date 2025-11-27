CREATE TABLE pedido_itens (
    id_pedido_item BIGSERIAL PRIMARY KEY,
    id_pedido BIGINT,
    id_produto BIGINT,
    quantidade INT,
    CONSTRAINT fk_item_pedido FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido) ON DELETE CASCADE,
    CONSTRAINT fk_item_produto FOREIGN KEY (id_produto) REFERENCES produtos(id_produto) ON DELETE CASCADE
);