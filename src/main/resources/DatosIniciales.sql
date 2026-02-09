-- 1. CATEGORIAS
INSERT INTO categories (name, parent_id) VALUES ('Musica', NULL) ON CONFLICT DO NOTHING;
INSERT INTO categories (name, parent_id) VALUES ('Cocina', NULL) ON CONFLICT DO NOTHING;
INSERT INTO categories (name, parent_id) VALUES ('Computacion', NULL) ON CONFLICT DO NOTHING;
INSERT INTO categories (name, parent_id) VALUES ('Stereo', 1) ON CONFLICT DO NOTHING;
INSERT INTO categories (name, parent_id) VALUES ('Teclado', 3) ON CONFLICT DO NOTHING;
INSERT INTO categories (name, parent_id) VALUES ('Parlante', 1) ON CONFLICT DO NOTHING;
INSERT INTO categories (name, parent_id) VALUES ('Portatil', 9) ON CONFLICT DO NOTHING;


-- 2. PRODUCTOS (Agregamos todos los Panacom que faltaban)
INSERT INTO product (title, price, stock, description) VALUES 
('Panacom CA5023', 44799, 10, 'Stereo Panacom High Power'),
('Panacom CA5025', 47399, 5, 'Stereo Panacom Bluetooth'),
('Panacom CA5032', 54299, 8, 'Stereo Panacom LED Display'),
('Panacom CA5089', 67299, 3, 'Stereo Panacom Premium'),
('Panacom CA5200', 82499, 4, 'Stereo Panacom Pro Series'),
('Panacom CA5104', 122999, 2, 'Stereo Panacom Ultra V2'),
('Panacom CA5102', 142199, 6, 'Stereo Panacom Elite'),
('Teclado Gamer', 25000, 15, 'Teclado Mecanico RGB');

-- 3. IMÁGENES (Asociamos cada foto a su producto buscando por el título)
-- Usamos INSERT INTO ... SELECT para no hardcodear IDs que Hibernate genera solo.
INSERT INTO image (product_id, url) SELECT id_producto, 'panacom-ca5023.jpg' FROM product WHERE title = 'Panacom CA5023';
INSERT INTO image (product_id, url) SELECT id_producto, 'panacom-ca5025.jpg' FROM product WHERE title = 'Panacom CA5025';
INSERT INTO image (product_id, url) SELECT id_producto, 'panacom-ca5032.jpg' FROM product WHERE title = 'Panacom CA5032';
INSERT INTO image (product_id, url) SELECT id_producto, 'panacom-ca5089.jpg' FROM product WHERE title = 'Panacom CA5089';
INSERT INTO image (product_id, url) SELECT id_producto, 'panacom-ca5200.jpg' FROM product WHERE title = 'Panacom CA5200';
INSERT INTO image (product_id, url) SELECT id_producto, 'panacom-ca5104.jpg' FROM product WHERE title = 'Panacom CA5104';
INSERT INTO image (product_id, url) SELECT id_producto, 'panacom-ca5102.jpg' FROM product WHERE title = 'Panacom CA5102';
INSERT INTO image (product_id, url) SELECT id_producto, 'TecladoGamer1.jpg' FROM product WHERE title = 'Teclado Gamer';
INSERT INTO image (product_id, url) SELECT id_producto, 'TecladoGamer2.jpg' FROM product WHERE title = 'Teclado Gamer';
INSERT INTO image (product_id, url) SELECT id_producto, 'TecladoGamer3.jpg' FROM product WHERE title = 'Teclado Gamer';
INSERT INTO image (product_id, url) SELECT id_producto, 'TecladoGamer4.jpg' FROM product WHERE title = 'Teclado Gamer';

-- 4. CATEGORÍAS DE PRODUCTO (Relación Muchos a Muchos)
INSERT INTO product_categories (product_id, category_id) 
SELECT p.id_producto, c.id FROM product p, categories c WHERE p.title LIKE 'Panacom%' AND c.name = 'Stereo';

INSERT INTO product_categories (product_id, category_id) 
SELECT p.id_producto, c.id FROM product p, categories c WHERE p.title = 'Teclado Gamer' AND c.name = 'Teclado';

-- 5. USUARIOS
INSERT INTO usuarios (username, password, role) VALUES ('german', '1234', 'CLIENTE') ON CONFLICT DO NOTHING;
INSERT INTO usuarios (username, password, role) VALUES ('laura', '1234', 'CLIENTE') ON CONFLICT DO NOTHING;