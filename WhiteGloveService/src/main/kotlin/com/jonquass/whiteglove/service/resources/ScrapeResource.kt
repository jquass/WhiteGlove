package com.jonquass.whiteglove.service.resources

import com.google.inject.Inject
import com.jonquass.whiteglove.core.api.v1.response.Response
import com.jonquass.whiteglove.core.api.v1.response.ResponseType
import com.jonquass.whiteglove.core.api.v1.scrape.ScrapeRequest
import com.jonquass.whiteglove.core.web.page.ScrapedPage
import com.jonquass.whiteglove.data.web.WebScraper
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/scrape")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ScrapeResource @Inject constructor(private var webScraper: WebScraper) {

    @POST
    fun scrapeUrl(pageRequest: ScrapeRequest): Response<ScrapedPage> {
        val scrapedPage: ScrapedPage? = webScraper.scrapeLink(pageRequest)
        return Response(ResponseType.SUCCESS, scrapedPage)
    }

}