package com.jonquass.whiteglove.data.web

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jonquass.whiteglove.core.jdbi.page.Page
import com.jonquass.whiteglove.data.jdbi.page.PageDbManager
import crawlercommons.robots.SimpleRobotRules
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI
import kotlin.math.min

@Singleton
class WebCrawler @Inject constructor(
    private var webScraper: WebScraper,
    private var pageDbManager: PageDbManager,
    private var robotsTxtClient: RobotsTxtClient,
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val hardLimit: Int = 1_000_000
    private val defaultBatchSize: Int = 100

    fun crawlDomain(link: URI, limit: Int? = hardLimit) {
        logger.debug("Crawling URI {} with limit {}", link, limit)
        val robotsTxt = robotsTxtClient.fetchRobotsTxt(link)

        if (robotsTxt.isAllowNone) {
            logger.info("Robots.txt is allow none $link")
            return
        }

        // TODO Process sitemap(s)

        robotsTxt.sitemaps

        val crawlLimit = min(limit!!, hardLimit)
        val batchSize = min(defaultBatchSize, crawlLimit)
        var pagesCount = 0
        var pages: List<Page>
        do {
            pages = pageDbManager.listForCrawling(link.host, batchSize)
            pages.forEach {
                processPage(it, robotsTxt)
            }
            pagesCount += pages.size
        } while (pages.isNotEmpty() && crawlLimit > pagesCount)
    }

    private fun processPage(page: Page, robotsTxt: SimpleRobotRules) {
        logger.info("Processing Page ${page.id} ${page.link}")
        webScraper.scrapeLink(page.link, robotsTxt)
    }

}