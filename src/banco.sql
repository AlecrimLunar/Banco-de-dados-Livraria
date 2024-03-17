SELECT * FROM vendedor;
CREATE TABLE Vendedor(
    id_vendedor SMALLSERIAL,
    nome varchar(30) NOT NULL,
    usuario varchar(15) UNIQUE NOT NULL,
    cpf varchar(11) UNIQUE NOT NULL,
    senha varchar(15) NOT NULL,

    PRIMARY KEY (id_vendedor)
);

SELECT * FROM cliente;
CREATE TABLE Cliente(
    id_cliente SMALLSERIAL,
    senha varchar(15) NOT NULL,
    usuario varchar(30) UNIQUE NOT NULL,
    nome varchar(30) NOT NULL,
    cpf varchar(11) UNIQUE NOT NULL,
    rua text,
    numero smallint,
    email text,
    is_flamengo boolean NOT NULL,
    is_sousa boolean NOT NULL,
    one_piece boolean NOT NULL,

    PRIMARY KEY (id_cliente)
);

SELECT * FROM Compra;

CREATE TABLE Compra(
    id_compra SMALLSERIAL,
    forma_pagamento text NOT NULL,
    data date NOT NULL,
    valor int NOT NULL CHECK (valor >= 0),
    id_vendedor int DEFAULT -1,
    id_carrinho int DEFAULT -1,
    id_cliente int DEFAULT -1,

    PRIMARY KEY  (id_compra),
    FOREIGN KEY (id_vendedor) REFERENCES Vendedor(id_vendedor) ON UPDATE CASCADE ON DELETE SET DEFAULT,
    FOREIGN KEY (id_carrinho) REFERENCES carrinho(id_carrinho) ON UPDATE CASCADE ON DELETE SET DEFAULT,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente) ON UPDATE CASCADE ON DELETE SET DEFAULT
);

SELECT * FROM livro;
CREATE TABLE livro(
    id_livro SMALLSERIAL PRIMARY KEY,
    tipo text NOT NULL CHECK (tipo in ('novo', 'usado')),
    nome varchar(30) NOT NULL,
    quantidade_estoque int NOT NULL CHECK (quantidade_estoque >= 0),
    from_mari boolean NOT NULL,
    autor varchar(30) NOT NULL,
    genero varchar(20) NOT NULL,
    preco money NOT NULL CHECK (preco >= money(0))
);

SELECT * FROM carrinho;
CREATE TABLE carrinho(
    id_carrinho SMALLSERIAL,
    id_cliente int DEFAULT -1,
    id_compra int DEFAULT -1,

    PRIMARY KEY (id_carrinho),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON UPDATE CASCADE ON DELETE SET DEFAULT,
    FOREIGN KEY (id_compra) REFERENCES compra(id_compra) ON UPDATE CASCADE ON DELETE SET DEFAULT
);

SELECT * FROM carrinho_livro;
CREATE TABLE carrinho_livro(
    id_carrinho int DEFAULT -1,
    id_livro int DEFAULT -1,
    quantidade int CHECK (quantidade > 0),
    PRIMARY KEY (id_carrinho, id_livro),
    FOREIGN KEY (id_carrinho) REFERENCES carrinho(id_carrinho) ON UPDATE CASCADE ON DELETE SET DEFAULT,
    FOREIGN KEY (id_livro) REFERENCES livro(id_livro) ON UPDATE CASCADE ON DELETE SET DEFAULT
);
