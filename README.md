# White Glove Search

Personalised search

## Purpose

This search engine is designed to be customized from the ground up.

Specific sites can be added or removed with ease, meaning searches are only on trusted sources.

The crawler respects a sites robots.txt file. The user agent is whiteglovebot.

Searches can be fine-tuned easily by selecting the sites to search and the algorithm to use.

All results are presented with a clear explanation of why.

## API Endpoints

Currently only the API is implemented, but a UI is planned

### Scrape

Scrape a single url

    POST    /api/v1/scrape    {"url":"https://www.website.com"}

- Fetches robots.txt for the domain
- Scrapes the page (if permitted by robots.txt)
    - Store the page HTML
    - Store the page headers
    - Store links found on the page
- Returns the scrape result

### Crawl

Scrapes all links stored in the db for an entire domain

    POST    /api/v1/crawl     {"url":"https://www.website.com", "limit": 100}

- Fetches robots.txt for the domain
    - Parses sitemap(s)
    - Stores links found in sitemap(s)
- Scrapes a page (if permitted by robots.txt)
    - Store the page HTML
    - Store the page headers
    - Store links found on the page
- Repeats scraping for other links in the db for the domain

### Search

    POST    /api/v1/search    {"search":"Where is Waldo", "limit": 100}

- Searches the stored html for matching pages using natural language mode
- Returns a list of search results

### Robots

    POST    /api/v1/robots    {"url":"https://www.website.com"}

- Fetches and returns robots.txt for the domain

