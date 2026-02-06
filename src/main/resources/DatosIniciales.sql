-- ... (Todo lo que ya tenías de categorías y productos) ...

-- 4. ASOCIACIÓN PRODUCTO-CATEGORÍA
INSERT INTO product_categories (product_id, category_id) SELECT id_producto, 4 FROM product WHERE id_producto BETWEEN 101 AND 107 ON CONFLICT DO NOTHING;
INSERT INTO product_categories (product_id, category_id) SELECT id_producto, 3 FROM product WHERE id_producto BETWEEN 201 AND 205 ON CONFLICT DO NOTHING;
INSERT INTO product_categories (product_id, category_id) SELECT id_producto, 5 FROM product WHERE id_producto IN (301, 302, 305) ON CONFLICT DO NOTHING;
INSERT INTO product_categories (product_id, category_id) SELECT id_producto, 6 FROM product WHERE id_producto IN (303, 304) ON CONFLICT DO NOTHING;
INSERT INTO product_categories (product_id, category_id) VALUES (401, 7), (401, 3), (601, 8), (601, 9) ON CONFLICT DO NOTHING;

-- 5. IMÁGENES (Aquí está lo que te falta)
-- Primero limpiamos para que el INSERT no falle si ya existen
DELETE FROM image;

-- Stereos con 4 fotos repetidas (para tus miniaturas)
-- 6. IMÁGENES (Solo archivos que existen físicamente)

-- Limpiamos todo primero para no tener duplicados "fantasmas"
DELETE FROM image;

-- STEREOS (4 fotos por producto, usando el archivo que corresponde a cada modelo)
INSERT INTO image (product_id, url) VALUES 
(101, 'panacom-ca5023.jpg'), (101, 'panacom-ca5023.jpg'), (101, 'panacom-ca5023.jpg'), (101, 'panacom-ca5023.jpg'),
(102, 'panacom-ca5025.jpg'), (102, 'panacom-ca5025.jpg'), (102, 'panacom-ca5025.jpg'), (102, 'panacom-ca5025.jpg'),
(103, 'panacom-ca5032.jpg'), (103, 'panacom-ca5032.jpg'), (103, 'panacom-ca5032.jpg'), (103, 'panacom-ca5032.jpg'),
(104, 'panacom-ca5089.jpg'), (104, 'panacom-ca5089.jpg'), (104, 'panacom-ca5089.jpg'), (104, 'panacom-ca5089.jpg'),
(105, 'panacom-ca5200.jpg'), (105, 'panacom-ca5200.jpg'), (105, 'panacom-ca5200.jpg'), (105, 'panacom-ca5200.jpg'),
(106, 'panacom-ca5104.jpg'), (106, 'panacom-ca5104.jpg'), (106, 'panacom-ca5104.jpg'), (106, 'panacom-ca5104.jpg'),
(107, 'panacom-ca5102.jpg'), (107, 'panacom-ca5102.jpg'), (107, 'panacom-ca5102.jpg'), (107, 'panacom-ca5102.jpg');

-- TECLADO (Tus 4 fotos distintas)
INSERT INTO image (product_id, url) VALUES 
(601, 'TecladoGamer1.jpg'), 
(601, 'TecladoGamer2.jpg'), 
(601, 'TecladoGamer3.jpg'), 
(601, 'TecladoGamer4.jpg');

-- NOTA: Los productos 201-205 y 301-305 no tendrán imágenes por ahora 
-- hasta que agregues los archivos .jpg a la carpeta /uploads.

-- Teclado con sus 4 fotos distintas
INSERT INTO image (product_id, url) VALUES 
(601, 'TecladoGamer1.jpg'), (601, 'TecladoGamer2.jpg'), (601, 'TecladoGamer3.jpg'), (601, 'TecladoGamer4.jpg');

-- Resto de productos
INSERT INTO image (product_id, url) VALUES 
(201, 'parlante.jpg'), (202, 'torre.jpg'), (203, 'pioneer69.jpg'), (204, 'subwoofer.jpg'), (205, 'tweeter.jpg'),
(301, 'horno.jpg'), (302, 'horno2.jpg'), (303, 'micro.jpg'), (304, 'micro2.jpg'), (305, 'horno3.jpg'), (401, 'vaso.jpg');