CREATE TABLE carrinho_itens (
    id_carrinho_item BIGSERIAL PRIMARY KEY,
    id_carrinho BIGINT NOT NULL,
    id_produto BIGINT NOT NULL,
    quantidade INT NOT NULL,
    CONSTRAINT fk_item_carrinho FOREIGN KEY (id_carrinho) REFERENCES carrinhos(id_carrinho) ON DELETE CASCADE,
    CONSTRAINT fk_item_produto FOREIGN KEY (id_produto) REFERENCES produtos(id_produto) ON DELETE CASCADE
);
