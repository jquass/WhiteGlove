package com.jonquass.whiteglove.service.resources

import com.google.inject.Inject
import com.jonquass.whiteglove.core.api.v1.crawl.CrawlRequest
import com.jonquass.whiteglove.core.api.v1.response.Response
import com.jonquass.whiteglove.core.api.v1.response.ResponseType
import com.jonquass.whiteglove.data.web.WebCrawler
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.apache.commons.lang3.ObjectUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Path("/crawl")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class CrawlResource @Inject constructor(private var webCrawler: WebCrawler) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @POST
    fun crawlUrl(crawlRequest: CrawlRequest): Response<ObjectUtils.Null> {
        logger.debug("CrawlRequest {}", crawlRequest)
        webCrawler.crawlDomain(crawlRequest)
        return Response(ResponseType.SUCCESS)
    }

}