CREATE TABLE items_categories (
                                  item_id BIGINT NOT NULL,
                                  category_id BIGINT NOT NULL,
                                  PRIMARY KEY (item_id, category_id),
                                  FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
                                  FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE INDEX idx_items_categories_category_id ON items_categories(category_id);