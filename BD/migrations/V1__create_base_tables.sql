CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    login TEXT NOT NULL,
    password TEXT NOT NULL,
    name TEXT NOT NULL,
    email TEXT,
    phone TEXT,
    status TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS addresses (
    address_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    country TEXT NOT NULL,
    region TEXT NOT NULL,
    city TEXT NOT NULL,
    house_number TEXT NOT NULL,
    apartment_number INT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_addresses_user_id
    ON addresses (user_id);

CREATE TABLE IF NOT EXISTS product_categories (
    product_category_id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS product_subcategories (
    product_subcategory_id SERIAL PRIMARY KEY,
    product_category_id INT REFERENCES product_categories(product_category_id) ON DELETE CASCADE ON UPDATE CASCADE,
    name TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_product_subcategories_product_category_id
    ON product_subcategories (product_category_id);

CREATE TABLE IF NOT EXISTS products (
    product_id SERIAL PRIMARY KEY,
    product_subcategory_id INT REFERENCES product_subcategories(product_subcategory_id) ON DELETE CASCADE ON UPDATE CASCADE,
    name TEXT NOT NULL,
    product_number INT NOT NULL,
    brand TEXT,
    description TEXT,
    color TEXT,
    material TEXT,
    country_origin TEXT
);

CREATE INDEX IF NOT EXISTS idx_products_product_subcategory_id
    ON products (product_subcategory_id);

CREATE TABLE IF NOT EXISTS sellers (
    seller_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_sellers_user_id
    ON sellers (user_id);

CREATE TABLE IF NOT EXISTS product_details (
    product_details_id SERIAL PRIMARY KEY,
    product_id INT REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE,
    seller_id INT REFERENCES sellers(seller_id) ON DELETE CASCADE ON UPDATE CASCADE,
    size_eu INT NOT NULL,
    price DECIMAL NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_product_details_product_id
    ON product_details (product_id);

CREATE INDEX IF NOT EXISTS idx_product_details_seller_id
    ON product_details (seller_id);

CREATE TABLE IF NOT EXISTS product_price_histories (
    product_price_history_id SERIAL PRIMARY KEY,
    product_id INT REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE,
    price DECIMAL NOT NULL,
    change_date TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_product_price_histories_product_id
    ON product_price_histories (product_id);

CREATE INDEX IF NOT EXISTS idx_product_price_histories_change_date
    ON product_price_histories (change_date);

CREATE TABLE IF NOT EXISTS cart_items (
    cart_item_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    product_id INT REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE,
    quantity INT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_cart_items_user_id
    ON cart_items (user_id);

CREATE INDEX IF NOT EXISTS idx_cart_items_product_id
    ON cart_items (product_id);

CREATE TABLE IF NOT EXISTS favorites (
    favorite_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE,
    product_id INT REFERENCES products(product_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_favorites_user_id
    ON favorites (user_id);

CREATE INDEX IF NOT EXISTS idx_favorites_product_id
    ON favorites (product_id);

CREATE TABLE IF NOT EXISTS review_sellers (
    review_seller_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    seller_id INT REFERENCES sellers(seller_id) ON DELETE CASCADE ON UPDATE CASCADE,
    rating INT NOT NULL,
    comment TEXT,
    create_date TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_review_sellers_user_id
    ON review_sellers (user_id);

CREATE INDEX IF NOT EXISTS idx_review_sellers_seller_id
    ON review_sellers (seller_id);

CREATE INDEX IF NOT EXISTS idx_review_sellers_create_date
    ON review_sellers (create_date);
