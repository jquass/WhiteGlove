# White Glove Search

Personalised search

## Purpose

This search engine is designed to be customized from the ground up.

Specific sites can be added or removed with ease, meaning searches are only on trusted sources.

The crawler respects a sites robots.txt file. The user agent is tbd (Whiteglovebot?)

Searches can be fine-tuned easily by selecting the sites to search and the algorithm to use.

All results are presented with a clear explanation of why.

## API Endpoints

Currently only the API is implemented, but a UI is planned

### Scrape

Scrape a single url

    POST    /api/v1/scrape    {"url":"https://www.website.com"}

- Fetches robots.txt for the domain
- Scrapes the page (if permitted by robots.txt)
    - Stores the page HTML
    - Stores the headers
    - Stores links

### Crawl

Crawl an entire domain

    POST    /api/v1/crawl     {"url":"https://www.website.com", "limit": 100}

- Fetches robots.txt for the domain
    - Parses sitemap(s)
        - Stores links
- Scrapes a page (if permitted by robots.txt)
    - Store the page HTML
    - Store the headers
    - Store links
- Fetches links from the db and attempts to scrape them

### Search

    POST    /api/v1/search    {"search":"Where is Waldo", "limit": 100}

- Searches for matching pages using natural language mode

### Robots

    POST    /api/v1/robots    {"url":"https://www.website.com"}

- Fetch robots.txt for the domain

