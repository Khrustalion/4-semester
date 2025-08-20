CREATE TABLE payment_cards (
    payment_card_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    card_number TEXT NOT NULL,
    expiration_date TEXT NOT NULL,
    save_date TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_payment_cards_user_id
    ON payment_cards (user_id);

CREATE INDEX IF NOT EXISTS idx_payment_cards_save_date
    ON payment_cards (save_date);

CREATE TABLE promotion_headers (
    promotion_header_id SERIAL PRIMARY KEY,
    product_category_id INT REFERENCES product_categories(product_category_id) ON DELETE CASCADE ON UPDATE CASCADE,
    percentage_discount INT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_promotion_headers_product_category_id
    ON promotion_headers (product_category_id);

CREATE TABLE promotion_details (
    promotion_detail_id SERIAL PRIMARY KEY,
    promotion_header_id INT REFERENCES promotion_headers(promotion_header_id) ON DELETE CASCADE ON UPDATE CASCADE,
    seller_id INT REFERENCES sellers(seller_id)
);

CREATE INDEX IF NOT EXISTS idx_promotion_details_promotion_header_id
    ON promotion_details (promotion_header_id);

CREATE INDEX IF NOT EXISTS idx_promotion_details_seller_id
    ON promotion_details (seller_id);

CREATE TABLE promo_codes (
    promo_code_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    code TEXT NOT NULL,
    discount_percent INT NOT NULL,
    is_personal BOOLEAN NOT NULL,
    expires_date TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_promo_codes_user_id
    ON promo_codes (user_id);

CREATE INDEX IF NOT EXISTS idx_promo_codes_expires_date
    ON promo_codes (expires_date);
