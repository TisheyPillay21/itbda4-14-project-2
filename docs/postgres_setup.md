# PostgreSQL setup for Q4

## Option A: Docker (quickest)
    docker run --name pg-bookstore -e POSTGRES_PASSWORD=choose_a_password -p 5432:5432 -d postgres:16
    docker exec -it pg-bookstore psql -U postgres -c "CREATE DATABASE bookstore;"

The SQL script uses `\copy` (client-side), so run it with a psql installed on your machine from the repository root:

    psql -h localhost -U postgres -d bookstore -f sql/q4_postgres.sql

If you only have psql inside the container, copy the CSV in first:

    docker cp data/clean_books.csv pg-bookstore:/tmp/clean_books.csv

then change the path in the `\copy` line to `/tmp/clean_books.csv` and run the script inside the container.

## Option B: local install
Install PostgreSQL from postgresql.org (includes psql and pgAdmin), then:

    psql -U postgres -c "CREATE DATABASE bookstore;"
    psql -U postgres -d bookstore -f sql/q4_postgres.sql

## Evidence to capture
Screenshots of: table creation, `SELECT COUNT(*)`, and the output of both analytical queries.
Never commit passwords; use environment variables (PGPASSWORD) or a local .Renviron file (git-ignored).
