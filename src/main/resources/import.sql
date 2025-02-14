INSERT INTO autores (data_hora_criacao, descricao, email, nome) VALUES ('2025-01-01 10:00:00', 'Autor de livros de ficção científica.', 'autor1@example.com', 'Autor 1');

INSERT INTO categorias (nome) VALUES ('Ficção Científica');
INSERT INTO categorias (nome) VALUES ('História');
INSERT INTO categorias (nome) VALUES ('Romance');

INSERT INTO livros (id, autor_id, categoria_id, data_publicacao, isbn, numero_paginas, preco, resumo, sumario, titulo) VALUES (default, 1, 1, '2025-12-12', 'abcasdf123', 100, 20, 'um resumo', 'um sumário bem grande', 'Mãe Ganso');
INSERT INTO livros (id, autor_id, categoria_id, data_publicacao, isbn, numero_paginas, preco, resumo, sumario, titulo) VALUES (default, 1, 2, '2025-06-15', 'abcasdf012', 250, 35.90, 'Livro de programação', 'Um sumário muito interessante', 'Java');

INSERT INTO paises (id, nome) VALUES (default, 'Brasil');
INSERT INTO paises (id, nome) VALUES (default, 'Argentina');
INSERT INTO paises (id, nome) VALUES (default, 'Filipinas');

INSERT INTO estados (id, nome, pais_id) VALUES (default, 'São Paulo', 1);
INSERT INTO estados (id, nome, pais_id) VALUES (default, 'Catamarca', 2);

insert into cupons_desconto (codigo, percentual_desconto, validade) values ('CUPOMVENCIDO', 10.00, '2023-01-01')
insert into cupons_desconto (codigo, percentual_desconto, validade) values ('CUPOM10', 10.00, '2050-01-01')

insert into pagamentos (cep, cidade, complemento, cpf_cnpj, cupons_desconto_codigo, email, endereco, estado_id, nome, pais_id, sobre_nome, telefone, total, data_compra) values ('12345-678', 'São Paulo', 'Apto 101', '704.724.660-62', 'CUPOM10', 'cliente@teste.com', 'Rua Teste, 123', 1, 'Nome Teste', 1, 'Sobrenome Teste', '(11) 99999-9999', 60.00, now());
insert into itens (livro_id, pagamento_id, preco, quantidade) values (1, 1, 20.00, 3);
insert into pagamentos (cep, cidade, complemento, cpf_cnpj, cupons_desconto_codigo, email, endereco, estado_id, nome, pais_id, sobre_nome, telefone, total, data_compra) values ('12345-678', 'Cebu', 'Apto 101', '704.724.660-62', null, 'cliente@teste.com', 'Rua Teste, 123', null, 'Nome Teste', 3, 'Sobrenome Teste', '(11) 99999-9999', 35.90, now());
insert into itens (livro_id, pagamento_id, preco, quantidade) values (2, 2, 35.90, 1);
