-- 1. CATEGORIAS
INSERT INTO categories (name, parent_id) VALUES ('Musica', NULL) ON CONFLICT DO NOTHING;--1
INSERT INTO categories (name, parent_id) VALUES ('Cocina', NULL) ON CONFLICT DO NOTHING;--2--
INSERT INTO categories (name, parent_id) VALUES ('Computacion', NULL) ON CONFLICT DO NOTHING;--3
INSERT INTO categories (name, parent_id) VALUES ('Stereo', 1) ON CONFLICT DO NOTHING;--4
INSERT INTO categories (name, parent_id) VALUES ('Teclado', 3) ON CONFLICT DO NOTHING;--5
INSERT INTO categories (name, parent_id) VALUES ('Parlante', 1) ON CONFLICT DO NOTHING;--6
INSERT INTO categories (name, parent_id) VALUES ('Portatil', 9) ON CONFLICT DO NOTHING;--7
INSERT INTO categories (name, parent_id) VALUES ('Mueble', NULL) ON CONFLICT DO NOTHING;--8
INSERT INTO categories (name, parent_id) VALUES ('Baño', NULL) ON CONFLICT DO NOTHING;--9
INSERT INTO categories (name, parent_id) VALUES ('Alfombra', NULL) ON CONFLICT DO NOTHING;--10
INSERT INTO categories (name, parent_id) VALUES ('Cafetera', 2) ON CONFLICT DO NOTHING;--11
INSERT INTO categories (name, parent_id) VALUES ('Dormitorio', NULL) ON CONFLICT DO NOTHING;--12
INSERT INTO categories (name, parent_id) VALUES ('Patio', NULL) ON CONFLICT DO NOTHING;--13--
INSERT INTO categories (name, parent_id) VALUES ('Jardin', NULL) ON CONFLICT DO NOTHING;--14
INSERT INTO categories (name, parent_id) VALUES ('Pileta', NULL) ON CONFLICT DO NOTHING;--15
INSERT INTO categories (name, parent_id) VALUES ('Deporte', NULL) ON CONFLICT DO NOTHING;--16
INSERT INTO categories (name, parent_id) VALUES ('Teatro', NULL) ON CONFLICT DO NOTHING;--17
INSERT INTO categories (name, parent_id) VALUES ('Amplificador', 1) ON CONFLICT DO NOTHING;--18
INSERT INTO categories (name, parent_id) VALUES ('Vasos', 1) ON CONFLICT DO NOTHING;--19
INSERT INTO categories (name, parent_id) VALUES ('Guitarras', 1) ON CONFLICT DO NOTHING;--20
INSERT INTO categories (name, parent_id) VALUES ('Baterías', 1) ON CONFLICT DO NOTHING;--21
INSERT INTO categories (name, parent_id) VALUES ('Teclados', 1) ON CONFLICT DO NOTHING;--22
INSERT INTO categories (name, parent_id) VALUES ('Portátil', 1) ON CONFLICT DO NOTHING;--23

-- 2. PRODUCTOS (Agregamos todos los Panacom que faltaban)
INSERT INTO product (title, price, stock, description, fecha_ultimo_precio, oculto) VALUES 
('Panacom CA5023', 44799, 10, 'Stereo Panacom High Power', CURRENT_DATE, false),
('Panacom CA5025', 47399, 5, 'Stereo Panacom Bluetooth',CURRENT_DATE, false),
('Panacom CA5032', 54299, 8, 'Stereo Panacom LED Display',CURRENT_DATE, false),
('Panacom CA5089', 67299, 3, 'Stereo Panacom Premium',CURRENT_DATE, false),
('Panacom CA5200', 82499, 4, 'Stereo Panacom Pro Series',CURRENT_DATE, false),
('Panacom CA5104', 122999, 2, 'Stereo Panacom Ultra V2',CURRENT_DATE, false),
('Panacom CA5102', 142199, 6, 'Stereo Panacom Elite',CURRENT_DATE, false),
('Teclado Gamer', 25000, 15, 'Teclado Mecanico RGB',CURRENT_DATE, false);

INSERT INTO product (title, price, stock, description, fecha_ultimo_precio, oculto) VALUES 
('Winco W-1926', 137499, 10, 'CAFETERA EXPRESS W1926 NEGRA* Cafetera compatible con capsulas Nespresso * Seleccion expresso o lungo * Tanque de 0,9 lts de capacidad * Diseño moderno y practico * Indicador luminoso * Colector de capsulas usadas*  Plataforma plegable para taza pequeña * Bomba de 20 BAR', CURRENT_DATE, false),
('Winco W-1925', 47399, 5, '15 BAR de presión. Tanque de agua translúcido extraíble. Capacidad 1 Ltr. Caldera de fundición de aluminio. Espumador de leche regulable. Bandeja calienta tazas. Filtro para una o dos tazas. Botón de vapor.Bandeja de goteo removible.Protección por sobrecalentamiento.',CURRENT_DATE, false),
('Winco W-1927', 188999, 5, 'CAFETERA EXPRESS DIGITAL/TOUCH W1927 - Cafetera expresso - Bomba de 20 BAR - Tanque de agua removible - Panel touch - Visor de temperatura digital - Diseño moderno y practico - Vaporizador de leche - Filtro para 1 o 2 pocillos - Color: Blanca - Negra',CURRENT_DATE, false),
('Winco W-1921 Blanca', 237999, 5, 'Bandeja superior calienta tazas. Visor de temperatura. Indicador de encendido. Bandeja antigoteo extraíble. Adaptador 3 en 1 para café molido y cápsulas sistema Nespresso y Dolce Gusto. 19 BAR de presión. Regulador de vapor.Tanque de agua de 1 litro. Espumador de leche. Potencia: 1050 W.',CURRENT_DATE, false),
('Winco W-1923', 260999, 5, 'CAFETERA EXPRESS W1923 * Cafetera express de 19 BAR de presion * Bomba Italiana * Espumador de leche * Filtro para una o dos tazas * Apta para capsulas * Boton de vapor * Bandeja antigoteo * Pantalla LCD * Bandeja superior calienta tazas * Potencia 1350W',CURRENT_DATE, false);

INSERT INTO product (title, price, stock, description) VALUES 
('ALFOMBRA DE BAÑO MEMORY - BLANCO PARIS -', 5999, 1,'- Medidas: 40 x 60 cm.
- Suave y absorbente.
- Antideslizante.
- Hipoalergénica.'),
('Pistola de Agua Eléctrica - WEAL MAKER', 58999, 1, '- Medidas: 26 x 32 cm.
- Pistola recargable.
- Alcance: 5-6 metros 
- Incluye: 1 cargador de agua estándar, 1 cargador de agua de alta capacidad, 1 batería recargable 3.7C Y 1 cargador a USB.'),
('MASAJEADOR RECARGABLE CON MANGO EXTENSIBLE',35999, 1,'- Potencia: 17W.
- 9 velocidades para ajustar la intensidad.
- 9 programas automáticos según tus necesidades.
- 4 cabezales intercambiables.'),
('SOPORTE MOVIL DE 26" A 60" WAVETV. - W-3350M',26899, 1,'- Capacidad: 30kg.
- Vesa: 40 x 40 cm.
- Incluye kit de instalación de soporte y manual.
- Báscula: +2°, -18°.
- Rotación de 180°.'),
('LICUADORA C/ JARRA DE PLÁSTICO 1LT - WHITENBLACK',59999, 1, '- Capacidad: 1.5 litros.
- Jarra de plástico con pico vertedor y tapa extraíble.
- Potencia: 400W.
- 2 velocidades + pulsador.
- Cuchillas de acero inoxidable.
- Base antideslizante'),
('PICADORA DE CARNE 800W - DAEWOO', 94999,1,'- Potencia: 800W.
- Dos discos de corte de 3 y 5 mm.
- Incluye empujador de carne.
- Con bandeja receptora.
- Fácil traslado.');

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
INSERT INTO image (product_id, url) SELECT id_producto, 'Winco W-1926 Negra.jpg' FROM product WHERE title = 'Winco W-1926';
INSERT INTO image (product_id, url) SELECT id_producto, 'Winco W-1926 Blanca.jpg' FROM product WHERE title = 'Winco W-1926';
INSERT INTO image (product_id, url) SELECT id_producto, 'Winco W-1925.jpg' FROM product WHERE title = 'Winco W-1925';
INSERT INTO image (product_id, url) SELECT id_producto, 'Winco W-1927 Blanca.jpg' FROM product WHERE title = 'Winco W-1927';
INSERT INTO image (product_id, url) SELECT id_producto, 'Winco W-1927 Negra.jpg' FROM product WHERE title = 'Winco W-1927';
INSERT INTO image (product_id, url) SELECT id_producto, 'Winco W-1921 Blanca.jpg' FROM product WHERE title = 'Winco W-1921 Blanca';
INSERT INTO image (product_id, url) SELECT id_producto, 'Winco W-1923.jpg' FROM product WHERE title = 'Winco W-1923';

-- 4. CATEGORÍAS DE PRODUCTO (Relación Muchos a Muchos)
INSERT INTO product_categories (product_id, category_id) 
SELECT p.id_producto, c.id FROM product p, categories c WHERE p.title LIKE 'Panacom%' AND c.name = 'Stereo';

INSERT INTO product_categories (product_id, category_id) 
SELECT p.id_producto, c.id FROM product p, categories c WHERE p.title = 'Teclado Gamer' AND c.name = 'Teclado';
-- 1. Vinculamos los productos que faltaban a sus categorías
INSERT INTO product_categories (product_id, category_id) 
SELECT id_producto, (SELECT id FROM categories WHERE name = 'Alfombra') FROM product WHERE title LIKE '%ALFOMBRA%';

INSERT INTO product_categories (product_id, category_id) 
SELECT id_producto, (SELECT id FROM categories WHERE name = 'Cocina') FROM product WHERE title IN ('LICUADORA C/ JARRA DE PLÁSTICO 1LT - WHITENBLACK', 'PICADORA DE CARNE 800W - DAEWOO');

INSERT INTO product_categories (product_id, category_id) 
SELECT p.id_producto, c.id FROM product p, categories c WHERE p.title LIKE 'Winco%' AND c.name = 'Cafetera';

-- 2. Si la Pistola no aparece, es porque falta la categoría 'Juguetes' o similar. 
INSERT INTO categories (name, parent_id) VALUES ('Juguetes', NULL) ON CONFLICT DO NOTHING;
INSERT INTO product_categories (product_id, category_id) 
SELECT id_producto, (SELECT id FROM categories WHERE name = 'Juguetes') FROM product WHERE title LIKE '%Pistola%';

-- 3. IMPORTANTE: Arreglamos los valores nulos que rompen el Home
UPDATE product SET oculto = false WHERE oculto IS NULL;
UPDATE product SET fecha_ultimo_precio = CURRENT_DATE WHERE fecha_ultimo_precio IS NULL;

-- 5. USUARIOS
INSERT INTO usuarios (username, password, role) VALUES ('german', '1234', 'CLIENTE') ON CONFLICT DO NOTHING;
INSERT INTO usuarios (username, password, role) VALUES ('laura', '1234', 'CLIENTE') ON CONFLICT DO NOTHING;