package com.jonquass.whiteglove.service.resources

import com.google.inject.Inject
import com.jonquass.whiteglove.core.web.response.ResponseType
import com.jonquass.whiteglove.core.web.web.Page
import com.jonquass.whiteglove.core.web.web.Request
import com.jonquass.whiteglove.core.web.web.Response
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
class WebResource {

    @Inject
    lateinit var webCrawler: WebCrawler

    @Inject
    lateinit var webScraper: WebScraper

    @POST
    @Path("/crawl")
    fun crawlUrl(request: Request): Response {
        webCrawler.crawlUrl(request)
        return Response(ResponseType.SUCCESS)
    }

    @POST
    @Path("/scrape")
    fun scrapeUrl(request: Request): Response {
        val page: Page = webScraper.scrapeUrl(request)
        return Response(ResponseType.SUCCESS, page)
    }

}