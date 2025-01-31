package com.jonquass.whiteglove.data.web

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jonquass.whiteglove.core.api.v1.crawl.CrawlRequest
import com.jonquass.whiteglove.core.jdbi.page.Page
import com.jonquass.whiteglove.data.jdbi.page.PageDbManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI
import kotlin.math.min

@Singleton
class WebCrawler @Inject constructor(
    private var webScraper: WebScraper,
    private var pageDbManager: PageDbManager,
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val hardLimit: Int = 1_000_000
    private val batchSize: Int = 100

    fun crawlDomain(crawlRequest: CrawlRequest) {
        crawlDomain(crawlRequest.url, crawlRequest.limit)
    }

    fun crawlDomain(link: URI, limit: Int? = hardLimit) {
        val crawlLimit = min(limit!!, hardLimit)
        var pagesCount = 0
        var pages: List<Page>
        do {
            pages = pageDbManager.listForCrawling(link.host, batchSize)
            pages.forEach(this::processPage)
            pagesCount += pages.size
        } while (pages.isNotEmpty() && crawlLimit > pagesCount)
    }

    private fun processPage(page: Page) {
        logger.info("Processing Page ${page.id} ${page.link}")
        webScraper.scrapeLink(page.link)
    }

}