# ITBDA4-14 Project 2

**Student:** Tishey Pillay (EDUV12760295)
**Repository:** https://github.com/TisheyPillay21/itbda4-14-project-2

Big data and text analytics project covering MapReduce, topic modelling, web text mining and an ETL pipeline into PostgreSQL. See `docs/project_documentation.md` for a summary of each question and the evidence.

| Folder | Content |
|---|---|
| `q1-mapreduce/` | Java Hadoop MapReduce keyword search (Q1) |
| `notebooks/` | R Markdown notebooks and knitted reports for Q2, Q3 and Q4 |
| `data/` | Raw and cleaned datasets (Q4) |
| `sql/` | PostgreSQL schema, import and analytical queries |
| `docs/` | Project documentation, setup notes and evidence images |

## Reproduce
1. Q1: `cd q1-mapreduce && mvn clean package`, then run as described in its README (it was run on Hadoop 3.3.6 in local mode).
2. Q2 and Q3: place `news.zip` in `notebooks/` (Q2), then knit the notebooks.
3. Q4: knit `notebooks/Q4_ETL_scrape_clean_load.Rmd`, then run `psql -d bookstore -f sql/q4_postgres.sql` from the repository root.

Data sources: books.toscrape.com (scraping sandbox), Eurac Research blog (CC BY 4.0), supplied news files (`news.zip`, `map_reduce_test_docs.zip`).
