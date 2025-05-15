-- Удаление данных в правильной последовательности

-- Категории (родительские и подкатегории)
insert INTO categories (id,name,parent_category,parent_category_id)
    VALUES
        ( 1,'Смартфоны',NULL,1),
        ( 2,'Аудиотехника',NULL,1),
        ( 7,'Аксессуары',NULL,1),
        ( 45,'Apple',NULL,1); -- <== ДОБАВЛЕНО



-- Product properties
INSERT INTO product_property (product_property_id) VALUES
(111),(112),(113),(121),(122),(123),(124),(131),(132),(133),
(211),(212),(213),(214),(215),(216),(217),(218),(219),(220),
(221),(222),(223),(224),(225),(226),(311),(312),(313),(314),
(315),(316),(317),(318),(319),(320),(321),(322),(323),(324),
(325),(326),(327),(328),(329),(330),(331);

-- Properties

INSERT INTO property (property_id, product_property_id, name, property_value) VALUES
(1, 111, 'Модель', 'Apple IPhone 12'),
(2, 111, 'Память', '128 Гб'),
(3, 111, 'Цвет', 'Белый'),
(4, 111, 'Емкость аккумулятора', '2815 мАч'),
(5, 111, 'Страна производитель', 'Китай'),
(6, 112, 'Модель', 'Apple IPhone 13'),
(7, 112, 'Память', '256 Гб'),
(8, 112, 'Цвет', 'Белый'),
(9, 112, 'Емкость аккумулятора', '2815 мАч'),
(10, 112, 'Страна производитель', 'Китай');


INSERT INTO items (id, category_id, name, description, count, image, price, discount_price, discount, product_property_id) VALUES
                                                                                                                               (1, 45, 'Apple IPhone 12', 'Дисплей', 3, 'https://cdn.svyaznoy.ru/upload/iblock/7f2/ruru_iphone12_q121_white_pdp-image-1b.jpg/resize/453x480/', 65000, 63000, TRUE, 111),
                                                                                                                               (3, 45, 'Apple IPhone 14', 'Защита экрана Ceramic Shield', 5, 'https://cdn.svyaznoy.ru/upload/iblock/000/0004936c8766c3d968e4ee3e56daf053.jpg/resize/453x480//', 89000, 75000, TRUE, 111);


-- Пользователи
INSERT INTO users (id, email, password, username) VALUES
    (1, 'test@gmail.com', '$2a$10$VCYlJrpY40ooY7iD8Mfd/e.vlCvcd2zxWlvx1jX1Bz.pM6w5H53Je', 'test@gmail.com');
