package com.jonquass.whiteglove.service.resources

import com.google.inject.Inject
import com.jonquass.whiteglove.core.web.Page
import com.jonquass.whiteglove.core.web.Request
import com.jonquass.whiteglove.core.web.response.Response
import com.jonquass.whiteglove.core.web.response.ResponseType
import com.jonquass.whiteglove.data.web.WebScraper
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/web")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class WebResource @Inject constructor(private var webScraper: WebScraper) {

    @POST
    @Path("/scrape")
    fun scrapeUrl(request: Request): Response {
        val page: Page = webScraper.scrapeLink(request)
        return Response(ResponseType.SUCCESS, page)
    }

}
