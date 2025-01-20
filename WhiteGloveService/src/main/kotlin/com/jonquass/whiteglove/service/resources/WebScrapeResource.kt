package com.jonquass.whiteglove.service.resources

import com.jonquass.whiteglove.core.web.response.ResponseType
import com.jonquass.whiteglove.core.web.webscrape.Page
import com.jonquass.whiteglove.core.web.webscrape.WebScrapeRequest
import com.jonquass.whiteglove.core.web.webscrape.WebScrapeResponse
import com.jonquass.whiteglove.data.webscrape.WebScraper
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/web-scrape")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class WebScrapeResource {

    private var webScraper: WebScraper = WebScraper()

    @POST
    fun webScrapeUrl(webScrapeRequest: WebScrapeRequest) : WebScrapeResponse {
        val page: Page = webScraper.scrapeUrl(webScrapeRequest)
        return WebScrapeResponse(ResponseType.SUCCESS, page)
    }

}