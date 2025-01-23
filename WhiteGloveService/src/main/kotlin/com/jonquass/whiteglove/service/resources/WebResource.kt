package com.jonquass.whiteglove.service.resources

import com.google.inject.Inject
import com.jonquass.whiteglove.core.web.page.PageRequest
import com.jonquass.whiteglove.core.web.page.ScrapedPage
import com.jonquass.whiteglove.core.web.response.Response
import com.jonquass.whiteglove.core.web.response.ResponseType
import com.jonquass.whiteglove.data.web.WebCrawler
import com.jonquass.whiteglove.data.web.WebScraper
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/web")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class WebResource @Inject constructor(
    private var webScraper: WebScraper,
    private var webCrawler: WebCrawler,
) {

    @POST
    @Path("/scrape")
    fun scrapeUrl(pageRequest: PageRequest): Response {
        val scrapedPage: ScrapedPage? = webScraper.scrapeLink(pageRequest)
        return Response(ResponseType.SUCCESS, scrapedPage)
    }

    @POST
    @Path("/crawl")
    fun crawlUrl(pageRequest: PageRequest) {
        webCrawler.crawlLink(pageRequest)
    }

}
