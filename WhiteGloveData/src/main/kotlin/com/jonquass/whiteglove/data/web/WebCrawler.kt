package com.jonquass.whiteglove.data.web

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jonquass.whiteglove.core.jdbi.page.Page
import com.jonquass.whiteglove.core.web.page.PageRequest
import com.jonquass.whiteglove.data.jdbi.page.PageDbManager
import kotlin.math.min

@Singleton
class WebCrawler @Inject constructor(
    private var webScraper: WebScraper,
    private var pageDbManager: PageDbManager,
) {

    private val hardLimit: Int = 1_000_000
    private val batchSize: Int = 100

    fun crawlLink(pageRequest: PageRequest) {
        val limit: Int = if (pageRequest.limit == null) {
            hardLimit
        } else {
            min(pageRequest.limit!!, hardLimit)
        }
        var pagesCount = 0
        var pages: List<Page>
        do {
            pages = pageDbManager.listForCrawling(pageRequest.url.host, batchSize)
            pages.forEach(this::processPage)
            pagesCount += pages.size
        } while (pages.isNotEmpty() && limit > pagesCount)
    }

    private fun processPage(page: Page) {
        println("Processing Page ${page.id} ${page.link}")
        webScraper.scrapeLink(page.link)
    }

}