# ./backups/Dockerfile
FROM postgres:latest

USER root

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
      cron \
      procps \
    && rm -rf /var/lib/apt/lists/*

COPY db_backup.sh /usr/local/bin/db_backup.sh
RUN chmod +x /usr/local/bin/db_backup.sh

COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

VOLUME ["/backups"]

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["cron", "-f", "-L", "15"]
