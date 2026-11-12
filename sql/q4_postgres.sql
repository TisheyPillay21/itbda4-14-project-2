

DROP TABLE IF EXISTS books;

CREATE TABLE books (
    upc             TEXT PRIMARY KEY,
    title           TEXT NOT NULL,
    category        TEXT,
    price_gbp       NUMERIC(6,2) CHECK (price_gbp >= 0),
    rating          SMALLINT CHECK (rating BETWEEN 1 AND 5),
    stock           INTEGER,
    in_stock        BOOLEAN,
    desc_words      INTEGER,
    mentioned_year  INTEGER,
    description     TEXT,
    detail_url      TEXT
);

-- Import the cleaned CSV (client-side copy, so no server file permissions needed)
\copy books FROM 'data/clean_books.csv' WITH (FORMAT csv, HEADER true)

-- Sanity checks
SELECT COUNT(*) AS rows_loaded FROM books;
SELECT upc, title, category, price_gbp, rating, stock, in_stock FROM books LIMIT 5;

-- Investigation 1: how do price and rating differ by category?
SELECT category,
       COUNT(*)                     AS n_books,
       ROUND(AVG(price_gbp), 2)     AS avg_price_gbp,
       ROUND(AVG(rating), 2)        AS avg_rating,
       SUM(stock)                   AS total_stock
FROM books
GROUP BY category
ORDER BY avg_price_gbp DESC;

-- Investigation 2: best value books - the cheapest 3 books with rating >= 4 in each category
SELECT category, title, price_gbp, rating
FROM (
    SELECT category, title, price_gbp, rating,
           ROW_NUMBER() OVER (PARTITION BY category ORDER BY price_gbp ASC, rating DESC) AS rn
    FROM books
    WHERE rating >= 4
) ranked
WHERE rn <= 3
ORDER BY category, price_gbp;

-- Optional extra: does price relate to rating?
SELECT rating, COUNT(*) AS n_books, ROUND(AVG(price_gbp), 2) AS avg_price_gbp
FROM books
GROUP BY rating
ORDER BY rating;
