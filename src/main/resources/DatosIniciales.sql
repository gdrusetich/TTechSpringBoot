-- 1. Categorías Padre
INSERT INTO categories (name, parent_id) VALUES ('Musica', NULL); -- ID 1
INSERT INTO categories (name, parent_id) VALUES ('Cocina', NULL); -- ID 2

INSERT INTO categories (name, parent_id) VALUES ('Parlante', 1); -- ID 3
INSERT INTO categories (name, parent_id) VALUES ('Stereo', 1);   -- ID 4

INSERT INTO product (title, price, stock, category_id, image_url) values ('Panacom CA5023', 44799, 1, 4,'panacom-ca5023.jpg');
INSERT INTO product (title, price, stock, category_id, image_url) values ('Panacom CA5025', 47399, 1, 4,'panacom-ca5025.jpg');
INSERT INTO product (title, price, stock, category_id, image_url) values ('Panacom CA5032', 54299, 1, 4,'panacom-ca5032.jpg');
INSERT INTO product (title, price, stock, category_id, image_url) values ('Panacom CA5089', 67299, 1, 4,'panacom-ca5089.jpg');
INSERT INTO product (title, price, stock, category_id, image_url) values ('Panacom CA5200', 82499, 1, 4,'panacom-ca5200.jpg');
INSERT INTO product (title, price, stock, category_id, image_url) values ('Panacom CA5104', 122999, 1, 4,'panacom-ca5104.jpg');
INSERT INTO product (title, price, stock, category_id, image_url) values ('Panacom CA5102', 142199, 1, 4,'panacom-ca5102.jpg');
