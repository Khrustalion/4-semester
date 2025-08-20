import os
import psycopg2
from faker import Faker
import random

env = os.getenv("APP_ENV", "prod")
if env != "dev":
    print("Skipping seeding (APP_ENV != dev)")
    exit(0)

seed_count = int(os.getenv("SEED_COUNT", "100"))
fake = Faker()

conn = psycopg2.connect(
    host=os.getenv("DB_HOST"),
    port=os.getenv("DB_PORT"),
    dbname=os.getenv("DB_NAME"),
    user=os.getenv("DB_USER"),
    password=os.getenv("DB_PASSWORD")
)

cur = conn.cursor()

def table_exists(table):
    cur.execute("""
        SELECT EXISTS (
            SELECT FROM information_schema.tables
            WHERE table_schema='public' AND table_name=%s
        )
    """, (table,))
    return cur.fetchone()[0]

def columns_for_table(table):
    cur.execute("""
        SELECT column_name FROM information_schema.columns
        WHERE table_schema='public' AND table_name=%s
    """, (table,))
    return [r[0] for r in cur.fetchall()]

SEED_COUNT = {
    "users": seed_count,
    "addresses": seed_count,
    "product_categories": int(0.1 * seed_count),
    "product_subcategories": int(0.2 * seed_count),
    "products": 3 * seed_count,
    "sellers": int(0.3 * seed_count),
    "product_details": 9 * seed_count,
    "product_price_histories": 9 * seed_count,
    "favorites": seed_count,
    "cart_items": seed_count,
    "payment_cards": seed_count,
    "promo_codes": int(0.5 * seed_count),
    "order_headers": 4 * seed_count,
    "order_details": 8 * seed_count,
    "review_products": seed_count,
    "tracking_order_headers": seed_count,
    "loyalty_point_histories": seed_count,
    "review_sellers": seed_count,
    "promotion_headers": int(0.1 * seed_count),
    "promotion_details": int(0.2 * seed_count)
}

SEED_TARGETS = {
    "users": lambda: {
        "login": fake.user_name(),
        "password": fake.password(),
        "name": fake.name(),
        "email": fake.email() if random.choice([True, False]) else None,
        "phone": fake.phone_number() if random.choice([True, False]) else None,
        "status": random.choice(["active", "inactive", "banned"])
    },

    "addresses": lambda: {
        "user_id": random.randint(1, SEED_COUNT["users"]),
        "country": fake.country(),
        "region": fake.state(),
        "city": fake.city(),
        "house_number": fake.building_number(),
        "apartment_number": random.randint(1, SEED_COUNT["users"])
    },

    "product_categories": lambda: {
        "name": fake.word().capitalize()
    },

    "product_subcategories": lambda: {
        "product_category_id": random.randint(1, SEED_COUNT["product_categories"]),
        "name": fake.word().capitalize()
    },

    "products": lambda: {
        "product_subcategory_id": random.randint(1, SEED_COUNT["product_subcategories"]),
        "name": fake.word().capitalize(),
        "product_number": random.randint(100, 9999),
        "brand": fake.company(),
        "description": fake.text(max_nb_chars=200),
        "color": fake.color_name(),
        "material": fake.word(),
        "country_origin": fake.country()
    },

    "sellers": lambda: {
        "user_id": random.randint(1, SEED_COUNT["users"])
    },

    "product_details": lambda: {
        "product_id": random.randint(1, SEED_COUNT["products"]),
        "seller_id": random.randint(1, SEED_COUNT["sellers"]),
        "size_eu": random.choice(range(36, 47)),
        "price": round(random.uniform(10, 1000), 2)
    },

    "product_price_histories": lambda: {
        "product_id": random.randint(1, SEED_COUNT["products"]),
        "price": round(random.uniform(10, 1000), 2),
        "change_date": fake.date_time_between(start_date="-1y", end_date="now")
    },

    "favorites": lambda: {
        "user_id": random.randint(1, SEED_COUNT["users"]),
        "product_id": random.randint(1, SEED_COUNT["products"])
    },

    "cart_items": lambda: {
        "user_id": random.randint(1, SEED_COUNT["users"]),
        "product_id": random.randint(1, SEED_COUNT["products"]),
        "quantity": random.randint(1, 5)
    },

    "payment_cards": lambda: {
        "user_id": random.randint(1, SEED_COUNT["users"]),
        "card_number": fake.credit_card_number(),
        "expiration_date": fake.credit_card_expire(),
        "save_date": fake.date_time_this_year()
    },

    "promo_codes": lambda: {
        "user_id": random.randint(1, SEED_COUNT["users"]),
        "code": fake.bothify(text='????-#####'),
        "discount_percent": random.randint(5, 30),
        "is_personal": random.choice([True, False]),
        "expires_date": fake.future_datetime(end_date="+30d"),
        "used": random.choice([True, False])
    },

    "order_headers": lambda: {
        "user_id": random.randint(1, SEED_COUNT["users"]),
        "address_id": random.randint(1, SEED_COUNT["addresses"]),
        "applied_promo_id": random.randint(1, SEED_COUNT["promo_codes"]),
        "payment_card_id": random.randint(1, SEED_COUNT["payment_cards"]),
        "order_date": fake.date_time_this_year(),
        "total_amount": round(random.uniform(50, 5000)),
        "used_loyalty_points": random.randint(0, SEED_COUNT["users"])
    },

    "order_details": lambda: {
        "order_header_id": random.randint(1, SEED_COUNT["order_headers"]),
        "product_details_id": random.randint(1, SEED_COUNT["product_details"]),
        "quantity": random.randint(1, 5)
    },

    "review_products": lambda: {
        "user_id": random.randint(1, SEED_COUNT["users"]),
        "order_header_id": random.randint(1, SEED_COUNT["order_headers"]),
        "product_id": random.randint(1, SEED_COUNT["products"]),
        "matching_size": random.randint(1, 5),
        "quality": random.randint(1, 5),
        "convenience": random.randint(1, 5),
        "comment": fake.sentence(),
        "rating": random.randint(1, 5),
        "create_date": fake.date_time_this_year()
    },

    "tracking_order_headers": lambda: {
        "order_header_id": random.randint(1, SEED_COUNT["order_headers"]),
        "status": random.choice(["processing", "shipped", "delivered", "cancelled"]),
        "start_date": fake.date_time_this_year(),
        "end_date": fake.future_datetime(end_date="+10d")
    },

    "loyalty_point_histories": lambda: {
        "user_id": random.randint(1, SEED_COUNT["users"]),
        "order_header_id": random.randint(1, SEED_COUNT["order_headers"]),
        "points": random.randint(0, 200),
        "earned_date": fake.past_datetime(start_date="-1y"),
        "expires_date": fake.future_datetime(end_date="+1y"),
        "spent": random.choice([True, False])
    },

    "review_sellers": lambda: {
        "user_id": random.randint(1, SEED_COUNT["users"]),
        "seller_id": random.randint(1, SEED_COUNT["sellers"]),
        "rating": random.randint(1, 5),
        "comment": fake.text(max_nb_chars=SEED_COUNT["users"]),
        "create_date": fake.date_time_this_year()
    },

    "promotion_headers": lambda: {
        "product_category_id": random.randint(1, SEED_COUNT["product_categories"]),
        "percentage_discount": random.randint(5, 30)
    },

    "promotion_details": lambda: {
        "promotion_header_id": random.randint(1, SEED_COUNT["promotion_headers"]),
        "seller_id": random.randint(1, SEED_COUNT["sellers"])
    }
}


for table, generator in SEED_TARGETS.items():
    if not table_exists(table):
        print(f"Skipping '{table}' (table does not exist)")
        continue

    cols = columns_for_table(table)
    rows = []
    for _ in range(SEED_COUNT[table]):
        entry = generator()
        filtered = {k: v for k, v in entry.items() if k in cols}
        if not filtered:
            continue
        rows.append(filtered)

    if not rows:
        continue

    keys = rows[0].keys()
    values_template = ','.join([f'%({k})s' for k in keys])
    insert_stmt = f"INSERT INTO {table} ({','.join(keys)}) VALUES ({values_template})"
    cur.executemany(insert_stmt, rows)
    print(f"Seeded {len(rows)} rows into '{table}'")

conn.commit()
cur.close()
conn.close()
