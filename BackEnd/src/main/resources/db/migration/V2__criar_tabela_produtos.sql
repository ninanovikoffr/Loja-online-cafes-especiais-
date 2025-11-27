CREATE TABLE produtos (
    id_produto BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100),
    descricao VARCHAR(255),
    preco VARCHAR(10),
    categoria VARCHAR(50),
    imagem_url VARCHAR(255),
    created_at TIMESTAMP
);
