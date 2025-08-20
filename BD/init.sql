CREATE EXTENSION IF NOT EXISTS dblink;
CREATE EXTENSION IF NOT EXISTS pg_stat_statements;

DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_user WHERE usename = 'admin') THEN
        CREATE USER admin WITH PASSWORD 'password' CREATEDB;
    END IF;
END
$$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'poizon') THEN
        PERFORM dblink_exec('dbname=postgres user=postgres password=postgres',
                            'CREATE DATABASE poizon OWNER admin');
    END IF;
    
END
$$;

\connect poizon

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_namespace WHERE nspname = 'public') THEN
        GRANT USAGE, CREATE ON SCHEMA public TO admin;
        ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO admin;
    END IF;
END
$$;