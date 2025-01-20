package com.jonquass.whiteglove.data.web

import com.google.inject.Inject
import com.jonquass.whiteglove.core.web.web.Page
import com.jonquass.whiteglove.core.web.web.Request

class WebCrawler {

    @Inject
    lateinit var webScraper: WebScraper

    fun crawlUrl(request: Request) {
        val page: Page = webScraper.scrapeUrl(request)
        // Process Links on Page here
    }


}