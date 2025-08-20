#!/usr/bin/env bash
set -eo pipefail

: "${BACKUP_RETENTION_COUNT:?не задано BACKUP_RETENTION_COUNT}"
: "${POSTGRES_DB:?не задано POSTGRES_DB}"
: "${POSTGRES_USER:?не задано POSTGRES_USER}"
: "${BACKUP_DIR:?не задано BACKUP_DIR}"
: "${POSTGRES_HOST:? Не задано POSTGRES_HOST}"

mkdir -p "$BACKUP_DIR"

TIMESTAMP=$(date +%Y%m%d%H%M%S)
FILENAME="${POSTGRES_DB}_${TIMESTAMP}.sql"

pg_dump -U "$POSTGRES_USER" -h "$POSTGRES_HOST" "$POSTGRES_DB" > "$BACKUP_DIR/$FILENAME"

cd "$BACKUP_DIR"
ls -1t *.sql | tail -n +"$((BACKUP_RETENTION_COUNT+1))" | xargs -r rm --
