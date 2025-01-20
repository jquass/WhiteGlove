package com.jonquass.whiteglove.data.web

import com.jonquass.whiteglove.core.web.web.Page
import com.jonquass.whiteglove.core.web.web.Request

class WebCrawler {

    private var webScraper: WebScraper = WebScraper()

    fun crawlUrl(request: Request) {
        val page: Page = webScraper.scrapeUrl(request)
        // Process Links on Page here
    }


}