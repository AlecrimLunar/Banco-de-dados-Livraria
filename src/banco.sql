CREATE SCHEMA Estoque;

CREATE TABLE Estoque.livro(
                              id_livro SMALLSERIAL,
                              tipo text NOT NULL CHECK (tipo in ('novo', 'usado')),
                              nome varchar(30) NOT NULL,
                              quantidade_estoque int NOT NULL CHECK (quantidade_estoque >= 0),
                              from_mari boolean NOT NULL,
                              autor varchar(30) NOT NULL,
                              genero varchar(20) NOT NULL,
                              preco money NOT NULL CHECK (preco >= money(0)),

                              PRIMARY KEY (id_livro)
);
INSERT INTO Estoque.livro
VALUES (-1, 'novo', 'padrão', 0, false, 'padrão', 'padrão', 5);

CREATE SCHEMA Vendedores_Info;

CREATE TABLE Vendedores_Info.donoLivraria(
                                             id_donoLivraria SMALLSERIAL,
                                             nome varchar(60) NOT NULL,
                                             usuario varchar(15) NOT NULL,
                                             senha varchar(15) NOT NULL,
                                             cpf varchar(11) UNIQUE NOT NULL,

                                             PRIMARY KEY (id_donoLivraria)
);

INSERT INTO Vendedores_Info.donoLivraria
VALUES(0, 'Michel', 'michel', '123', '01234567891');

CREATE TABLE Vendedores_Info.vendedor(
                                         id_vendedor SMALLSERIAL,
                                         nome varchar(30) NOT NULL,
                                         usuario varchar(15) UNIQUE NOT NULL,
                                         cpf varchar(11) UNIQUE NOT NULL,
                                         senha varchar(15) NOT NULL,

                                         PRIMARY KEY (id_vendedor)
);
INSERT INTO Vendedores_Info.vendedor
VALUES (-1, 'padrão', 'user', '00000000000', '123456');

CREATE TABLE Vendedores_Info.relatorio(
                                          id_relatorio SMALLSERIAL,
                                          id_vendedor int DEFAULT -1 NOT NULL,
                                          data date NOT NULL,

                                          PRIMARY KEY (id_relatorio),
                                          FOREIGN KEY (id_vendedor) REFERENCES Vendedores_Info.vendedor(id_vendedor) ON UPDATE CASCADE ON DELETE SET DEFAULT
);
INSERT INTO Vendedores_Info.relatorio
VALUES (-1, -1, '1986-04-11');

CREATE TABLE Vendedores_Info.relatorio_venda(
                                                id_relatorio int DEFAULT -1 NOT NULL,
                                                id_venda int DEFAULT -1 NOT NULL,

                                                PRIMARY KEY (id_relatorio, id_venda),
                                                FOREIGN KEY (id_relatorio) REFERENCES Vendedores_Info.relatorio(id_relatorio) ON UPDATE CASCADE ON DELETE SET DEFAULT--,
    --FOREIGN KEY (id_venda) REFERENCES compras_info.compra(id_compra) ON UPDATE CASCADE ON DELETE SET DEFAULT
);
INSERT INTO Vendedores_Info.relatorio_venda
VALUES (-1, -1);

CREATE SCHEMA Clientes_Info;

--SELECT * FROM cliente;
CREATE TABLE Clientes_Info.cliente(
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
INSERT INTO Clientes_Info.cliente
VALUES (-1, '123456', 'user', 'padrão', '00000000001',
        'Rua da paz', 19, 'user@gmail.com', false, false, false);

CREATE TABLE Clientes_Info.carrinho(
                                       id_carrinho SMALLSERIAL,
                                       id_cliente int DEFAULT -1,
                                       id_compra int DEFAULT -1,

                                       PRIMARY KEY (id_carrinho),
                                       FOREIGN KEY (id_cliente) REFERENCES Clientes_Info.cliente(id_cliente) ON UPDATE CASCADE ON DELETE SET DEFAULT--,
    --FOREIGN KEY (id_compra) REFERENCES Compras_Info.compra(id_compra) ON UPDATE CASCADE ON DELETE SET DEFAULT
);
INSERT INTO Clientes_Info.carrinho
VALUES (-1, -1, -1);

CREATE TABLE Clientes_Info.carrinho_livro(
                                             id_carrinho int DEFAULT -1,
                                             id_livro int DEFAULT -1,
                                             quantidade int CHECK (quantidade > 0),

                                             PRIMARY KEY (id_carrinho, id_livro),
                                             FOREIGN KEY (id_carrinho) REFERENCES Clientes_Info.carrinho(id_carrinho) ON UPDATE CASCADE ON DELETE SET DEFAULT--,
    --FOREIGN KEY (id_livro) REFERENCES Estoque.livro(id_livro) ON UPDATE CASCADE ON DELETE SET DEFAULT
);
INSERT INTO Clientes_Info.carrinho_livro
VALUES (-1, -1, 1);

CREATE SCHEMA Compras_Info;

CREATE TABLE Compras_Info.compra(
                                    id_compra SMALLSERIAL,
                                    forma_pagamento text NOT NULL,
                                    data date NOT NULL,
                                    valor int NOT NULL CHECK (valor >= 0),
                                    id_vendedor int DEFAULT -1,
                                    id_carrinho int DEFAULT -1,
                                    id_cliente int DEFAULT -1,
                                    foiRecusada bool DEFAULT false,

                                    PRIMARY KEY  (id_compra)--,
    --FOREIGN KEY (id_vendedor) REFERENCES Vendedores_Info.vendedor(id_vendedor) ON UPDATE CASCADE ON DELETE SET DEFAULT,
    --FOREIGN KEY (id_carrinho) REFERENCES Clientes_Info.carrinho(id_carrinho) ON UPDATE CASCADE ON DELETE SET DEFAULT,
    --FOREIGN KEY (id_cliente) REFERENCES Clientes_Info.cliente(id_cliente) ON UPDATE CASCADE ON DELETE SET DEFAULT
);
INSERT INTO Compras_Info.compra
VALUES (-1, 'padrão', '1986-04-11', 0, -1, -1, -1, false);

ALTER TABLE Vendedores_Info.relatorio_venda
    ADD CONSTRAINT relatorio_venda_id_venda_fkey
        FOREIGN KEY (id_venda) REFERENCES compras_info.compra(id_compra)
            ON UPDATE CASCADE ON DELETE SET DEFAULT;

ALTER TABLE Clientes_Info.carrinho
    ADD CONSTRAINT carrinho_id_compra_fkey
        FOREIGN KEY (id_compra) REFERENCES Compras_Info.compra(id_compra)
            ON UPDATE CASCADE ON DELETE SET DEFAULT;

ALTER TABLE Clientes_Info.carrinho_livro
    ADD CONSTRAINT carrinho_livro_id_livro_fkey
        FOREIGN KEY (id_livro) REFERENCES Estoque.livro(id_livro)
            ON UPDATE CASCADE ON DELETE SET DEFAULT;


ALTER TABLE Compras_Info.compra
    ADD CONSTRAINT compra_id_vendedor
        FOREIGN KEY (id_vendedor) REFERENCES Vendedores_Info.vendedor(id_vendedor)
            ON UPDATE CASCADE ON DELETE SET DEFAULT;

ALTER TABLE Compras_Info.compra
    ADD CONSTRAINT compra_id_carrinho
        FOREIGN KEY (id_carrinho) REFERENCES Clientes_Info.carrinho(id_carrinho)
            ON UPDATE CASCADE ON DELETE SET DEFAULT;
ALTER TABLE Compras_Info.compra
    ADD CONSTRAINT compra_id_cliente
        FOREIGN KEY (id_cliente) REFERENCES Clientes_Info.cliente(id_cliente)
            ON UPDATE CASCADE ON DELETE SET DEFAULT;

SELECT * FROM Vendedores_Info.vendedor;

CREATE ROLE adm LOGIN PASSWORD '12' SUPERUSER;

CREATE ROLE dono_livraria LOGIN PASSWORD '1234';
GRANT SELECT ON ALL TABLES IN SCHEMA Compras_Info, Clientes_Info, Vendedores_Info, Estoque TO dono_livraria;
GRANT INSERT ON ALL TABLES IN SCHEMA Compras_Info, Clientes_Info, Vendedores_Info, Estoque  TO dono_livraria;
GRANT UPDATE ON ALL TABLES IN SCHEMA Compras_Info, Clientes_Info, Vendedores_Info, Estoque  TO dono_livraria;
GRANT DELETE ON Estoque.livro, Clientes_Info.cliente,
    Vendedores_Info.vendedor TO dono_livraria;



CREATE ROLE vendedor_role LOGIN PASSWORD '123456';
GRANT SELECT ON Vendedores_Info.vendedor, Estoque.livro, Compras_Info.compra,
    Clientes_Info.cliente, Clientes_Info.carrinho, Clientes_Info.carrinho_livro
    TO vendedor_role;
GRANT INSERT ON Vendedores_Info.vendedor, Estoque.livro, Compras_Info.compra,
    Clientes_Info.cliente TO vendedor_role;
GRANT UPDATE ON Vendedores_Info.vendedor, Estoque.livro, Compras_Info.compra,
    Clientes_Info.cliente TO vendedor_role;
GRANT DELETE ON Clientes_Info.cliente TO vendedor_role;

CREATE ROLE cliente_role LOGIN PASSWORD '12345678';
GRANT SELECT ON Estoque.livro, Clientes_Info.cliente, Clientes_Info.carrinho,
    Clientes_Info.carrinho_livro, Compras_Info.compra TO cliente_role;
GRANT INSERT ON Clientes_Info.cliente, Clientes_Info.carrinho,
    Clientes_Info.carrinho_livro, Compras_Info.compra TO cliente_role;
GRANT UPDATE ON Clientes_Info.cliente, Clientes_Info.carrinho_livro TO cliente_role;
GRANT DELETE ON Clientes_Info.carrinho_livro, Clientes_Info.cliente TO cliente_role;

GRANT USAGE ON SCHEMA vendedores_info, Clientes_Info, Compras_Info, Estoque TO vendedor_role;
GRANT USAGE ON SCHEMA Clientes_Info, Compras_Info, Estoque TO cliente_role;
GRANT USAGE ON SCHEMA vendedores_info, Clientes_Info, Compras_Info, Estoque TO dono_livraria;

GRANT USAGE ON ALL SEQUENCES IN SCHEMA Clientes_Info, Compras_Info, Estoque TO cliente_role;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA vendedores_info, Clientes_Info, Compras_Info, Estoque TO vendedor_role;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA vendedores_info, Clientes_Info, Compras_Info, Estoque TO dono_livraria;

CREATE VIEW destaques AS
SELECT l.* FROM estoque.livro AS l
WHERE l.id_livro >= 0 AND l.quantidade_estoque > 0 AND l.id_livro IN
                                                       (SELECT cl.id_livro FROM clientes_info.carrinho_livro AS cl
                                                        GROUP BY cl.id_livro ORDER BY COUNT(*) DESC LIMIT 3);
                                                        

CREATE OR REPLACE PROCEDURE criar_relatorio(IN mes_entrada INT, IN ano_entrada INT)
LANGUAGE PLPGSQL
AS $$
BEGIN

  INSERT INTO Vendedores_Info.relatorio(id_vendedor, data)
  SELECT DISTINCT c.id_vendedor, DATE_TRUNC('MONTH', c.data)
  FROM Compras_Info.compra AS c
  WHERE EXTRACT(MONTH FROM c.data) = mes_entrada
  AND EXTRACT(YEAR FROM c.data) = ano_entrada;

  INSERT INTO Vendedores_Info.relatorio_venda(id_relatorio, id_venda)
  SELECT r.id_relatorio, c.id_compra
  FROM Vendedores_Info.relatorio AS r
  INNER JOIN Compras_Info.compra AS c ON r.id_vendedor = c.id_vendedor
  WHERE EXTRACT(MONTH FROM c.data) = mes_entrada
  AND EXTRACT(YEAR FROM c.data) = ano_entrada
  AND EXTRACT(MONTH FROM r.data) = mes_entrada
  AND EXTRACT(YEAR FROM r.data) = ano_entrada;

END $$;
