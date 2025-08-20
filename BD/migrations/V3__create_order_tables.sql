CREATE TABLE IF NOT EXISTS order_headers (
    order_header_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    address_id INT REFERENCES addresses(address_id) ON DELETE CASCADE ON UPDATE CASCADE,
    applied_promo_id INT REFERENCES promo_codes(promo_code_id) ON DELETE CASCADE ON UPDATE CASCADE,
    payment_card_id INT REFERENCES payment_cards(payment_card_id) ON DELETE CASCADE ON UPDATE CASCADE,
    order_date TIMESTAMP NOT NULL,
    total_amount DECIMAL NOT NULL,
    used_loyalty_points INT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_order_headers_user_id
    ON order_headers (user_id);

CREATE INDEX IF NOT EXISTS idx_order_headers_address_id
    ON order_headers (address_id);

CREATE INDEX IF NOT EXISTS idx_order_headers_applied_promo_id
    ON order_headers (applied_promo_id);

CREATE INDEX IF NOT EXISTS idx_order_headers_payment_card_id
    ON order_headers (payment_card_id);

CREATE INDEX IF NOT EXISTS idx_order_headers_order_date
    ON order_headers (order_date);

CREATE TABLE IF NOT EXISTS order_details (
    order_details_id SERIAL PRIMARY KEY,
    order_header_id INT REFERENCES order_headers(order_header_id) ON DELETE CASCADE ON UPDATE CASCADE,
    product_details_id INT REFERENCES product_details(product_details_id) ON DELETE CASCADE ON UPDATE CASCADE,
    quantity INT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_order_details_order_header_id
    ON order_details (order_header_id);

CREATE INDEX IF NOT EXISTS idx_order_details_product_details_id
    ON order_details (product_details_id);

CREATE TABLE IF NOT EXISTS tracking_order_headers (
    tracking_order_header_id SERIAL PRIMARY KEY,
    order_header_id INT REFERENCES order_headers(order_header_id) ON DELETE CASCADE ON UPDATE CASCADE,
    status TEXT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_tracking_order_headers_order_header_id
    ON tracking_order_headers (order_header_id);

CREATE INDEX IF NOT EXISTS idx_tracking_order_headers_start_date
    ON tracking_order_headers (start_date);

CREATE TABLE IF NOT EXISTS loyalty_point_histories (
    loyalty_point_history_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    order_header_id INT REFERENCES order_headers(order_header_id) ON DELETE CASCADE ON UPDATE CASCADE,
    points INT NOT NULL,
    earned_date TIMESTAMP NOT NULL,
    expires_date TIMESTAMP NOT NULL,
    spent BOOLEAN NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_loyalty_point_histories_user_id
    ON loyalty_point_histories (user_id);

CREATE INDEX IF NOT EXISTS idx_loyalty_point_histories_order_header_id
    ON loyalty_point_histories (order_header_id);

CREATE INDEX IF NOT EXISTS idx_loyalty_point_histories_earned_date
    ON loyalty_point_histories (earned_date);

CREATE INDEX IF NOT EXISTS idx_loyalty_point_histories_expires_date
    ON loyalty_point_histories (expires_date);

CREATE TABLE IF NOT EXISTS review_products (
    review_product_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    order_header_id INT REFERENCES order_headers(order_header_id) ON DELETE CASCADE ON UPDATE CASCADE,
    product_id INT REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE,
    matching_size INT NOT NULL,
    quality INT NOT NULL,
    convenience INT NOT NULL,
    comment TEXT,
    rating INT NOT NULL,
    create_date TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_review_products_user_id
    ON review_products (user_id);

CREATE INDEX IF NOT EXISTS idx_review_products_order_header_id
    ON review_products (order_header_id);

CREATE INDEX IF NOT EXISTS idx_review_products_product_id
    ON review_products (product_id);

CREATE INDEX IF NOT EXISTS idx_review_products_create_date
    ON review_products (create_date);
