import os
import psycopg2
from psycopg2 import sql

def main():
    db_params = {
        "host": os.getenv("DB_HOST", "postgres"),
        "port": os.getenv("DB_PORT", "5432"),
        "dbname": os.getenv("DB_NAME"),
        "user": os.getenv("DB_USER"),
        "password": os.getenv("DB_PASSWORD")
    }
    
    analyst_names = os.getenv("ANALYST_NAMES", "").split(",")
    analyst_names = [name.strip() for name in analyst_names if name.strip()]
    
    if not analyst_names:
        print("No analyst names provided in ANALYST_NAMES environment variable")
        return
    
    try:
        conn = psycopg2.connect(**db_params)
        conn.autocommit = True
        cursor = conn.cursor()
        
        print("Creating analytic role...")
        cursor.execute("""
            DO $$
            BEGIN
                IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'analytic') THEN
                    CREATE ROLE analytic NOLOGIN;
                    COMMENT ON ROLE analytic IS 'Group role for analysts with read-only access';
                END IF;
            END $$;
        """)
        
        cursor.execute("""
            GRANT USAGE ON SCHEMA public TO analytic;
            GRANT SELECT ON ALL TABLES IN SCHEMA public TO analytic;
            
            ALTER DEFAULT PRIVILEGES IN SCHEMA public
            GRANT SELECT ON TABLES TO analytic;
        """)
        
        for username in analyst_names:
            password = f"{username}_123"
            
            print(f"Creating user {username}...")
            cursor.execute(
                sql.SQL("""
                    DO $$
                    BEGIN
                        IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = {username}) THEN
                            EXECUTE format('CREATE USER %I WITH PASSWORD %L IN ROLE analytic', {username}, {password});
                        ELSE
                            EXECUTE format('GRANT analytic TO %I', {username});
                        END IF;
                    END $$;
                """).format(
                    username=sql.Literal(username),
                    password=sql.Literal(password)
                )
            )
        
        print("All operations completed successfully!")
        
    except Exception as e:
        print(f"Error: {e}")
    finally:
        if 'conn' in locals():
            conn.close()

if __name__ == "__main__":
    main()