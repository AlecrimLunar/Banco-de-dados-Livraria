CREATE TABLE Vendedor(
    id_vendedor SMALLSERIAL PRIMARY KEY,
    nome text,
    login_vendedor text UNIQUE,
    cpf bigint UNIQUE,
    senha_vendedor text
);
CREATE TABLE Cliente(
    id_cliente SMALLSERIAL PRIMARY KEY,
    senha_cliente text,
    login_cliente text UNIQUE,
    nome text,
    cpf bigint UNIQUE,
    login text,
    rua text,
    numero smallint,
    email text,
    is_flamengo boolean,
    is_sousa boolean,
    one_piece boolean
);

CREATE TABLE Compra(
    id_compra SMALLSERIAL PRIMARY KEY,
    forma_pagamento text,
    data date,
    valor int,
    id_vendedor int,
    id_carrinho int,
    id_cliente int
);

CREATE TABLE livro(
    id_livro SMALLSERIAL PRIMARY KEY,
    tipo text,
    nome text,
    quantidade_estoque int,
    from_mari boolean,
    autor text,
    genero text,
    preco float4
);

CREATE TABLE carrinho_livro(
    id_carrinho int,
    id_livro int,
    quantidade int,
    PRIMARY KEY (id_carrinho, id_livro)
);

CREATE TABLE carrinho(
    id_carrinho SMALLSERIAL PRIMARY KEY,
    id_cliente int,
    id_compra int
)
