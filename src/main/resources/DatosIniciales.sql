-- 0. Limpiar (En orden para no romper llaves foráneas)
DELETE FROM product_categories;
DELETE FROM image;
DELETE FROM product;

-- 1. Categorías
INSERT INTO categories (name, parent_id) VALUES ('Musica', NULL) ON CONFLICT (name) DO NOTHING;
INSERT INTO categories (name, parent_id) VALUES ('Cocina', NULL) ON CONFLICT (name) DO NOTHING;
INSERT INTO categories (name, parent_id) VALUES ('Parlante', (SELECT id FROM categories WHERE name='Musica')) ON CONFLICT (name) DO NOTHING;
INSERT INTO categories (name, parent_id) VALUES ('Stereo', (SELECT id FROM categories WHERE name='Musica')) ON CONFLICT (name) DO NOTHING;

-- 2. Productos (Sin ID manual)
INSERT INTO product (title, price, stock, description) VALUES 
('Panacom CA5023', 44799, 1, 'Stereo Panacom con Bluetooth y USB'),
('Panacom CA5025', 47399, 1, 'Stereo Panacom entrada auxiliar'),
('Panacom CA5032', 54299, 1, 'Stereo Panacom display LCD');

-- 3. Relación Producto <-> Categoría
-- Buscamos el ID del producto por su Título y el de la categoría por su Nombre
INSERT INTO product_categories (product_id, category_id) VALUES 
((SELECT id_producto FROM product WHERE title='Panacom CA5023'), (SELECT id FROM categories WHERE name='Stereo')), 
((SELECT id_producto FROM product WHERE title='Panacom CA5025'), (SELECT id FROM categories WHERE name='Stereo')), 
((SELECT id_producto FROM product WHERE title='Panacom CA5032'), (SELECT id FROM categories WHERE name='Stereo'));

-- 4. Imágenes
INSERT INTO image (product_id, url) VALUES 
((SELECT id_producto FROM product WHERE title='Panacom CA5023'), 'panacom-ca5023.jpg'), 
((SELECT id_producto FROM product WHERE title='Panacom CA5025'), 'panacom-ca5025.jpg'), 
((SELECT id_producto FROM product WHERE title='Panacom CA5032'), 'panacom-ca5032.jpg');