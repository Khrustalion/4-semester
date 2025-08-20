import os
import time
from datetime import datetime, timedelta

import psycopg2
from psycopg2.extras import RealDictCursor
from prometheus_client import start_http_server, Summary, Counter

QUERY_DURATION = Summary(
    'simulator_raw_query_duration_seconds',
    'Time spent executing a raw SQL query'
)
QUERY_TOTAL = Counter(
    'simulator_raw_query_total',
    'Total number of raw SQL queries executed by the simulator'
)

DB_USER     = os.getenv('POSTGRES_USER')
DB_PASSWORD = os.getenv('POSTGRES_PASSWORD')
DB_HOST     = os.getenv('POSTGRES_HOST', 'postgres')
DB_PORT     = os.getenv('POSTGRES_PORT', '5432')
DB_NAME     = os.getenv('POSTGRES_DB')

if not all([DB_USER, DB_PASSWORD, DB_HOST, DB_PORT, DB_NAME]):
    raise RuntimeError(
        "Не заданы переменные окружения: POSTGRES_USER, POSTGRES_PASSWORD, "
        "POSTGRES_HOST, POSTGRES_PORT или POSTGRES_DB"
    )

def get_connection():
    return psycopg2.connect(
        dbname=DB_NAME,
        user=DB_USER,
        password=DB_PASSWORD,
        host=DB_HOST,
        port=DB_PORT,
        cursor_factory=RealDictCursor
    )


@QUERY_DURATION.time()
def query_top_customers():
    """
    Выполнение одного «сложного» запроса: выбор содержимого последнего заказа
    для каждого пользователя (самый свежий по дате order_date).
    """
    QUERY_TOTAL.inc()
    conn = get_connection()
    try:
        cur = conn.cursor()
        sql = """
        WITH ranked_orders AS (
            SELECT 
                oh.user_id,
                a.country,
                a.region,
                a.city,
                a.house_number,
                a.apartment_number,
                pc.code            AS promo_code,
                pc.discount_percent,
                p.card_number,
                pr.name            AS product_name,
                pd.size_eu,
                pd.price,
                od.quantity,
                DENSE_RANK() OVER (
                  PARTITION BY u.user_id 
                  ORDER BY oh.order_date DESC
                ) AS rnk
            FROM users AS u
            JOIN order_headers    AS oh ON u.user_id = oh.user_id
            JOIN order_details    AS od ON od.order_header_id = oh.order_header_id
            JOIN addresses        AS a  ON a.address_id       = oh.address_id
            JOIN promo_codes      AS pc ON pc.promo_code_id   = oh.applied_promo_id
            JOIN payment_cards    AS p  ON p.payment_card_id  = oh.payment_card_id
            JOIN product_details  AS pd ON pd.product_details_id = od.product_details_id
            JOIN products         AS pr ON pr.product_id      = pd.product_id
        )
        SELECT 
            user_id,
            country,
            region,
            city,
            house_number,
            apartment_number,
            promo_code,
            discount_percent,
            card_number,
            product_name,
            size_eu,
            price,
            quantity
        FROM ranked_orders
        WHERE rnk = 1;
        """
        cur.execute(sql)
        rows = cur.fetchall()

        for r in rows:
            _ = (
                r['user_id'],
                r['product_name'],
                float(r['price']),
                int(r['quantity'])
            )
        cur.close()
    except Exception as e:
        print("Ошибка при выполнении query_top_customers():", e)
    finally:
        conn.close()


if __name__ == "__main__":
    start_http_server(8000)
    print("Prometheus metrics available on http://0.0.0.0:8000/metrics")

    INTERVAL_SEC = int(os.getenv("SIMULATOR_RAW_INTERVAL_SEC", "5"))
    print(f"Running single-threaded simulator; interval = {INTERVAL_SEC} seconds")

    try:
        while True:
            query_top_customers()
            
    except KeyboardInterrupt:
        print("Simulator interrupted, exiting…")
