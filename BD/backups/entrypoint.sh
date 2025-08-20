#!/usr/bin/env bash
set -eo pipefail

PGPASS_PATH="/root/.pgpass"

cat <<EOF > "$PGPASS_PATH"
${POSTGRES_HOST}:${POSTGRES_PORT}:${POSTGRES_DB}:${POSTGRES_USER}:${POSTGRES_PASSWORD}
EOF

chmod 600 "$PGPASS_PATH"
chown root:root "$PGPASS_PATH"

echo "${BACKUP_INTERVAL_CRON} POSTGRES_HOST=postgres POSTGRES_PORT=5432 POSTGRES_DB=poizon POSTGRES_USER=dbadmin POSTGRES_PASSWORD=password BACKUP_DIR=/backups BACKUP_RETENTION_COUNT=7 /usr/local/bin/db_backup.sh >> /var/log/db_backup.log 2>&1
" | crontab -

mkdir -p "${BACKUP_DIR}"

exec "$@"
