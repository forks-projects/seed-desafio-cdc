INSERT INTO autores (data_hora_criacao, descricao, email, nome) VALUES ('2025-01-01 10:00:00', 'Autor de livros de ficção científica.', 'autor1@example.com', 'Autor 1');

INSERT INTO categorias (nome) VALUES ('Ficção Científica');
INSERT INTO categorias (nome) VALUES ('História');
INSERT INTO categorias (nome) VALUES ('Romance');

INSERT INTO livros (id, autor_id, categoria_id, data_publicacao, isbn, numero_paginas, preco, resumo, sumario, titulo) VALUES (default, 1, 1, '2025-12-12', 'abcasdf123', 100, 20, 'um resumo', 'um sumário bem grande', 'Mãe Ganso');
INSERT INTO livros (id, autor_id, categoria_id, data_publicacao, isbn, numero_paginas, preco, resumo, sumario, titulo) VALUES (default, 1, 2, '2025-06-15', 'abcasdf012', 250, 35.90, 'Livro de programação', 'Um sumário muito interessante', 'Java');

INSERT INTO paises (id, nome) VALUES (default, 'Brasil');
INSERT INTO estados (id, nome, pais_id) VALUES (default, 'São Paulo', 1);
