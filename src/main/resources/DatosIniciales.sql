-- 1. CATEGORÍAS (Padres e Hijas)
INSERT INTO categories (id, name, parent_id) VALUES (1, 'Música', NULL) ON CONFLICT (id) DO NOTHING;
INSERT INTO categories (id, name, parent_id) VALUES (2, 'Cocina', NULL) ON CONFLICT (id) DO NOTHING;

INSERT INTO categories (id, name, parent_id) VALUES (3, 'Parlante', 1) ON CONFLICT (id) DO NOTHING;
INSERT INTO categories (id, name, parent_id) VALUES (4, 'Stereo', 1) ON CONFLICT (id) DO NOTHING;
INSERT INTO categories (id, name, parent_id) VALUES (5, 'Hornos', 2) ON CONFLICT (id) DO NOTHING;
INSERT INTO categories (id, name, parent_id) VALUES (6, 'Microondas', 2) ON CONFLICT (id) DO NOTHING;

-- 2. PRODUCTOS: STEREOS (IDs 101 al 107)
INSERT INTO product (id_producto, title, price, stock, description) VALUES 
(101, 'Panacom CA5023', 44799, 10, 'Stereo básico con frente desmontable, USB y entrada auxiliar. Ideal para reemplazo estándar.'),
(102, 'Panacom CA5025', 47399, 8, 'Versión mejorada con Bluetooth integrado y control remoto. Excelente relación precio-calidad.'),
(103, 'Panacom CA5032', 54299, 5, 'Iluminación LED ajustable y doble salida RCA para potencias. Sonido nítido.'),
(104, 'Panacom CA5089', 67299, 4, 'Pantalla LCD de alta visibilidad y sintonizador de radio de largo alcance.'),
(105, 'Panacom CA5200', 82499, 12, 'Modelo de alta gama con soporte para formatos FLAC y carga rápida de celular vía USB.'),
(106, 'Panacom CA5104', 122999, 3, 'Stereo Multimedia con pantalla táctil, compatible con cámara de retroceso.'),
(107, 'Panacom CA5102', 142199, 2, 'El tope de gama: MirrorLink, pantalla HD de 7 pulgadas y ecualizador de 10 bandas.');

-- 3. PRODUCTOS: PARLANTES (IDs 201 al 205)
INSERT INTO product (id_producto, title, price, stock, description) VALUES 
(201, 'Parlante Portátil BT10', 15000, 20, 'Parlante Bluetooth resistente a salpicaduras. 10W de potencia.'),
(202, 'Torre de Sonido Party', 85000, 5, 'Torre con luces LED rítmicas y entrada para micrófono. Ideal fiestas.'),
(203, 'Parlantes 6x9 Pioneer', 42000, 10, 'Parlantes triaxiales para auto. Potencia máxima 400W.'),
(204, 'Subwoofer 12 Slim', 78000, 4, 'Subwoofer chato para optimizar espacio en el baúl.'),
(205, 'Tweeters de Seda', 12000, 15, 'Kit de agudos para mejorar la definición del sonido en tu vehículo.');

-- 4. PRODUCTOS: COCINA (IDs 301 al 310)
INSERT INTO product (id_producto, title, price, stock, description) VALUES 
(301, 'Horno Eléctrico 30L', 95000, 6, 'Horno con convección y timer. Ideal para repostería casera.'),
(302, 'Horno Grill Premium', 115000, 4, '45 litros de capacidad, luz interior y doble estante.'),
(303, 'Microondas Digital 20L', 88000, 10, 'Panel táctil, 6 programas de cocción automática y descongelado rápido.'),
(304, 'Microondas con Grill 25L', 130000, 3, 'Función combinada de microondas y grill para dorar tus comidas.'),
(305, 'Horno Eléctrico Compacto', 65000, 8, 'Perfecto para departamentos pequeños. 20 litros de capacidad.');

-- 5. ASOCIACIÓN PRODUCTO-CATEGORÍA (Tabla Intermedia)
-- Stereos (Cat 4)
INSERT INTO product_categories (product_id, category_id) SELECT id_producto, 4 FROM product WHERE id_producto BETWEEN 101 AND 107;
-- Parlantes (Cat 3)
INSERT INTO product_categories (product_id, category_id) SELECT id_producto, 3 FROM product WHERE id_producto BETWEEN 201 AND 205;
-- Hornos (Cat 5)
INSERT INTO product_categories (product_id, category_id) SELECT id_producto, 5 FROM product WHERE id_producto IN (301, 302, 305);
-- Microondas (Cat 6)
INSERT INTO product_categories (product_id, category_id) SELECT id_producto, 6 FROM product WHERE id_producto IN (303, 304);

-- 6. IMÁGENES (Referencia básica para que no tire error el JS)
-- Aquí solo pongo una imagen para el primer stereo como ejemplo, tú completas el resto.
INSERT INTO image (product_id, url) VALUES (101, 'panacom-ca5023.jpg');

-- Limpiamos imágenes previas de stéreos para no duplicar si re-ejecutas
DELETE FROM image WHERE product_id BETWEEN 101 AND 107;

-- Insertamos 4 veces la misma imagen para cada Stereo (para probar las miniaturas)
-- Producto 101
INSERT INTO image (product_id, url) VALUES (101, 'panacom-ca5023.jpg'), (101, 'panacom-ca5023.jpg'), (101, 'panacom-ca5023.jpg'), (101, 'panacom-ca5023.jpg');
-- Producto 102
INSERT INTO image (product_id, url) VALUES (102, 'panacom-ca5025.jpg'), (102, 'panacom-ca5025.jpg'), (102, 'panacom-ca5025.jpg'), (102, 'panacom-ca5025.jpg');
-- Producto 103
INSERT INTO image (product_id, url) VALUES (103, 'panacom-ca5032.jpg'), (103, 'panacom-ca5032.jpg'), (103, 'panacom-ca5032.jpg'), (103, 'panacom-ca5032.jpg');
-- Producto 104
INSERT INTO image (product_id, url) VALUES (104, 'panacom-ca5089.jpg'), (104, 'panacom-ca5089.jpg'), (104, 'panacom-ca5089.jpg'), (104, 'panacom-ca5089.jpg');
-- Producto 105
INSERT INTO image (product_id, url) VALUES (105, 'panacom-ca5200.jpg'), (105, 'panacom-ca5200.jpg'), (105, 'panacom-ca5200.jpg'), (105, 'panacom-ca5200.jpg');
-- Producto 106
INSERT INTO image (product_id, url) VALUES (106, 'panacom-ca5104.jpg'), (106, 'panacom-ca5104.jpg'), (106, 'panacom-ca5104.jpg'), (106, 'panacom-ca5104.jpg');
-- Producto 107
INSERT INTO image (product_id, url) VALUES (107, 'panacom-ca5102.jpg'), (107, 'panacom-ca5102.jpg'), (107, 'panacom-ca5102.jpg'), (107, 'panacom-ca5102.jpg');

-- Imágenes para los otros productos (al menos una para que se vean en Similares)
INSERT INTO image (product_id, url) VALUES (201, 'parlante.jpg'), (202, 'torre.jpg'), (203, 'pioneer69.jpg'), (204, 'subwoofer.jpg'), (205, 'tweeter.jpg');
INSERT INTO image (product_id, url) VALUES (301, 'horno.jpg'), (302, 'horno2.jpg'), (303, 'micro.jpg'), (304, 'micro2.jpg'), (305, 'horno3.jpg');

-- 1. Limpiamos usuarios previos si es necesario
DELETE FROM usuarios WHERE username IN ('german', 'laura');

-- 2. Insertamos los nuevos usuarios con rol de CLIENTE
-- Nota: Si usas BCrypt, el hash de '1234' es: $2a$10$8.UnS8OWY7qBhV4VrkX.3u3yV.n6y.o6f8g6f8g6f8g6f8g6f8g6f
INSERT INTO usuarios (username, password, role) VALUES 
('german', '1234', 'CLIENTE'),
('laura', '1234', 'CLIENTE');