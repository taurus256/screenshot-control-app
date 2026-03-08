FROM postgres:15.10-alpine3.21
LABEL authors="taurus"
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD "P@$$w0rd"
COPY init.sql /docker-entrypoint-initdb.d/
EXPOSE 5432
