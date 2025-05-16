-- Удаление таблиц (в порядке зависимости)
DROP TABLE IF EXISTS buckets_items;
DROP TABLE IF EXISTS user_feedback;
DROP TABLE IF EXISTS refresh_token;
DROP TABLE IF EXISTS property;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS product_property;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- Удаление неиспользуемых или отсутствующих таблиц
DROP TABLE IF EXISTS photo;
DROP TABLE IF EXISTS items_categories;
DROP TABLE IF EXISTS buckets;

-- Создание последовательностей
CREATE SEQUENCE IF NOT EXISTS buckets_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS category_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS item_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS refresh_token_token_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS user_sequence START WITH 1 INCREMENT BY 1;

-- Таблица пользователей
CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       address VARCHAR(255),
                       email VARCHAR(255),
                       password VARCHAR(255),
                       username VARCHAR(255)
);

-- Категории
CREATE TABLE categories (
                            id BIGINT PRIMARY KEY ,
                            name VARCHAR(255),
                            parent_category VARCHAR(255),
                            parent_category_id BIGINT
);

-- Свойства товаров (общее)
CREATE TABLE product_property (
                                  product_property_id BIGINT PRIMARY KEY AUTO_INCREMENT
);

-- Товары
CREATE TABLE items (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       price BIGINT NOT NULL,
                       count BIGINT,
                       description VARCHAR(255),
                       image VARCHAR(255),
                       name VARCHAR(255),
                       product_property_id BIGINT NOT NULL,
                       discount_price BIGINT,
                       discount BOOLEAN NOT NULL,
                       category_id BIGINT NOT NULL
);

-- Свойства товара (детали)
CREATE TABLE property (
                          property_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          product_property_id BIGINT NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          property_value VARCHAR(255) NOT NULL
);

-- Таблица токенов обновления
CREATE TABLE refresh_token (
                               token_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id BIGINT,
                               token VARCHAR(255),
                               CONSTRAINT uq_refresh_token_user_id UNIQUE (user_id)
);

-- Отзывы пользователей
CREATE TABLE user_feedback (
                               user_feedback_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id BIGINT NOT NULL,
                               product_id BIGINT NOT NULL,
                               feedback BIGINT NOT NULL,
                               comment VARCHAR(500)
);

-- Связка корзины и товаров
CREATE TABLE buckets_items (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id BIGINT NOT NULL,
                               item_id BIGINT NOT NULL,
                               count INTEGER NOT NULL
);

-- Роли пользователей
CREATE TABLE user_role (
                           user_id BIGINT NOT NULL,
                           role VARCHAR(255) NOT NULL,
                           PRIMARY KEY (user_id, role)
);

-- Внешние ключи

-- Категории
ALTER TABLE categories
    ADD CONSTRAINT fk_categories_parent FOREIGN KEY (parent_category_id)
        REFERENCES categories (id) ON DELETE SET NULL;

-- Товары
ALTER TABLE items
    ADD CONSTRAINT fk_items_category FOREIGN KEY (category_id)
        REFERENCES categories (id) ON DELETE CASCADE;
ALTER TABLE items
    ADD CONSTRAINT fk_items_property FOREIGN KEY (product_property_id)
        REFERENCES product_property (product_property_id) ON DELETE CASCADE;

-- Свойства
ALTER TABLE property
    ADD CONSTRAINT fk_property_product_property FOREIGN KEY (product_property_id)
        REFERENCES product_property (product_property_id) ON DELETE CASCADE;

-- Токены
ALTER TABLE refresh_token
    ADD CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE;

-- Отзывы
ALTER TABLE user_feedback
    ADD CONSTRAINT fk_feedback_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE user_feedback
    ADD CONSTRAINT fk_feedback_product FOREIGN KEY (product_id)
        REFERENCES items (id) ON DELETE CASCADE;

-- Корзина
ALTER TABLE buckets_items
    ADD CONSTRAINT fk_buckets_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE buckets_items
    ADD CONSTRAINT fk_buckets_item FOREIGN KEY (item_id)
        REFERENCES items (id) ON DELETE CASCADE;

-- Роли
ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE;
