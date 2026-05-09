-- ===== Sabores =====
INSERT INTO sabores (nome, descricao, categoria, preco_adicional, ativo) VALUES
 ('Margherita',         'Molho de tomate, mussarela, manjericao fresco e azeite',     'TRADICIONAL', 8.00,  TRUE),
 ('Calabresa',          'Calabresa fatiada, cebola e azeitonas pretas',                'TRADICIONAL', 8.00,  TRUE),
 ('Quatro Queijos',     'Mussarela, provolone, parmesao e gorgonzola',                 'ESPECIAL',    14.00, TRUE),
 ('Frango com Catupiry','Frango desfiado, catupiry e milho',                           'ESPECIAL',    12.00, TRUE),
 ('Portuguesa',         'Presunto, ovos, cebola, ervilha e azeitonas',                 'TRADICIONAL', 10.00, TRUE),
 ('Pepperoni',          'Pepperoni picante e mussarela',                               'ESPECIAL',    14.00, TRUE),
 ('Vegetariana',        'Brocolis, tomate seco, cogumelos e mussarela',                'VEGETARIANA', 13.00, TRUE),
 ('Camarao',            'Camarao, catupiry e cheiro-verde',                            'PREMIUM',     22.00, TRUE),
 ('Chocolate Branco',   'Chocolate branco e morango',                                  'DOCE',        12.00, TRUE),
 ('Banana com Canela',  'Banana, canela e leite condensado',                           'DOCE',        9.00,  TRUE);

-- ===== Ingredientes =====
INSERT INTO sabor_ingredientes (sabor_id, ingrediente) VALUES
 ((SELECT id FROM sabores WHERE nome = 'Margherita'),         'molho de tomate'),
 ((SELECT id FROM sabores WHERE nome = 'Margherita'),         'mussarela'),
 ((SELECT id FROM sabores WHERE nome = 'Margherita'),         'manjericao'),
 ((SELECT id FROM sabores WHERE nome = 'Margherita'),         'azeite'),
 ((SELECT id FROM sabores WHERE nome = 'Calabresa'),          'calabresa'),
 ((SELECT id FROM sabores WHERE nome = 'Calabresa'),          'cebola'),
 ((SELECT id FROM sabores WHERE nome = 'Calabresa'),          'azeitona'),
 ((SELECT id FROM sabores WHERE nome = 'Calabresa'),          'mussarela'),
 ((SELECT id FROM sabores WHERE nome = 'Quatro Queijos'),     'mussarela'),
 ((SELECT id FROM sabores WHERE nome = 'Quatro Queijos'),     'provolone'),
 ((SELECT id FROM sabores WHERE nome = 'Quatro Queijos'),     'parmesao'),
 ((SELECT id FROM sabores WHERE nome = 'Quatro Queijos'),     'gorgonzola'),
 ((SELECT id FROM sabores WHERE nome = 'Frango com Catupiry'),'frango'),
 ((SELECT id FROM sabores WHERE nome = 'Frango com Catupiry'),'catupiry'),
 ((SELECT id FROM sabores WHERE nome = 'Frango com Catupiry'),'milho'),
 ((SELECT id FROM sabores WHERE nome = 'Portuguesa'),         'presunto'),
 ((SELECT id FROM sabores WHERE nome = 'Portuguesa'),         'ovo'),
 ((SELECT id FROM sabores WHERE nome = 'Portuguesa'),         'cebola'),
 ((SELECT id FROM sabores WHERE nome = 'Portuguesa'),         'ervilha'),
 ((SELECT id FROM sabores WHERE nome = 'Portuguesa'),         'azeitona'),
 ((SELECT id FROM sabores WHERE nome = 'Pepperoni'),          'pepperoni'),
 ((SELECT id FROM sabores WHERE nome = 'Pepperoni'),          'mussarela'),
 ((SELECT id FROM sabores WHERE nome = 'Vegetariana'),        'brocolis'),
 ((SELECT id FROM sabores WHERE nome = 'Vegetariana'),        'tomate seco'),
 ((SELECT id FROM sabores WHERE nome = 'Vegetariana'),        'cogumelos'),
 ((SELECT id FROM sabores WHERE nome = 'Vegetariana'),        'mussarela'),
 ((SELECT id FROM sabores WHERE nome = 'Camarao'),            'camarao'),
 ((SELECT id FROM sabores WHERE nome = 'Camarao'),            'catupiry'),
 ((SELECT id FROM sabores WHERE nome = 'Camarao'),            'cheiro-verde'),
 ((SELECT id FROM sabores WHERE nome = 'Chocolate Branco'),   'chocolate branco'),
 ((SELECT id FROM sabores WHERE nome = 'Chocolate Branco'),   'morango'),
 ((SELECT id FROM sabores WHERE nome = 'Banana com Canela'),  'banana'),
 ((SELECT id FROM sabores WHERE nome = 'Banana com Canela'),  'canela'),
 ((SELECT id FROM sabores WHERE nome = 'Banana com Canela'),  'leite condensado');

-- ===== Pizzas =====
INSERT INTO pizzas (nome, descricao, tamanho, disponivel) VALUES
 ('Margherita Tradicional', 'A classica italiana, simples e perfeita',   'MEDIA',   TRUE),
 ('Calabresa Familia',      'Calabresa fatiada com cebola para 8 pessoas','FAMILIA', TRUE),
 ('Quatro Queijos Grande',  'Para os amantes de queijo',                 'GRANDE',  TRUE),
 ('Pepperoni Pequena',      'Pepperoni para um almoco solo',              'PEQUENA', TRUE),
 ('Doce Banana',            'Banana com canela e leite condensado',       'MEDIA',   TRUE);

INSERT INTO pizza_sabores (pizza_id, sabor_id) VALUES
 ((SELECT id FROM pizzas WHERE nome = 'Margherita Tradicional'),(SELECT id FROM sabores WHERE nome = 'Margherita')),
 ((SELECT id FROM pizzas WHERE nome = 'Calabresa Familia'),     (SELECT id FROM sabores WHERE nome = 'Calabresa')),
 ((SELECT id FROM pizzas WHERE nome = 'Quatro Queijos Grande'), (SELECT id FROM sabores WHERE nome = 'Quatro Queijos')),
 ((SELECT id FROM pizzas WHERE nome = 'Pepperoni Pequena'),     (SELECT id FROM sabores WHERE nome = 'Pepperoni')),
 ((SELECT id FROM pizzas WHERE nome = 'Doce Banana'),           (SELECT id FROM sabores WHERE nome = 'Banana com Canela'));

-- ===== Cliente de exemplo =====
INSERT INTO clientes (
    nome, cpf, email,
    telefone_ddd, telefone_numero,
    endereco_logradouro, endereco_numero, endereco_complemento,
    endereco_bairro, endereco_cidade, endereco_uf, endereco_cep,
    data_cadastro
) VALUES (
    'Maria Souza', '123.456.789-00', 'maria.souza@email.com',
    '11', '987654321',
    'Rua das Flores', '123', 'Apto 45',
    'Centro', 'Sao Paulo', 'SP', '01001-000',
    CURRENT_TIMESTAMP
);
