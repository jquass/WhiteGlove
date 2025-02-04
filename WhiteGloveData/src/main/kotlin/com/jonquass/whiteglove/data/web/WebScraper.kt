package com.jonquass.whiteglove.data.web

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jonquass.whiteglove.core.api.v1.scrape.ScrapeRequest
import com.jonquass.whiteglove.core.web.page.PageSource
import com.jonquass.whiteglove.core.web.page.ScrapedPage
import com.jonquass.whiteglove.core.web.page.header.OGHeader
import com.jonquass.whiteglove.data.jdbi.page.PageDbManager
import com.jonquass.whiteglove.data.jdbi.page.header.PageHeaderDbManager
import crawlercommons.robots.SimpleRobotRules
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.URISyntaxException
import java.util.*

@Singleton
class WebScraper @Inject constructor(
    private var pageDbManager: PageDbManager,
    private var robotsTxtClient: RobotsTxtClient,
    private var robotsTxtManager: RobotsTxtManager,
    private var pageHeaderDbManager: PageHeaderDbManager,
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun scrapeLink(scrapeRequest: ScrapeRequest): ScrapedPage? {
        return scrapeLink(scrapeRequest.url)
    }

    private fun scrapeLink(link: URI): ScrapedPage? {
        val robotsTxt = robotsTxtClient.fetchRobotsTxt(link)
        return scrapeLink(link, robotsTxt)
    }

    fun scrapeLink(link: URI, robotsTxt: SimpleRobotRules): ScrapedPage? {
        logger.info("Scraping Link $link")
        if (link.scheme == null || link.host == null) {
            logger.warn("Skipping link without scheme and/or host $link")
            return null
        }

        if (!robotsTxtManager.shouldScrape(link, robotsTxt)) {
            logger.info("Skipping link due to robots.txt rules")
            return null
        }

        val doc: Document = fetchDocument(link) ?: return null

        val page = pageDbManager.upsert(link, doc, PageSource.SCRAPE)
        val headers: EnumMap<OGHeader, String> = getHeaders(doc)

        // TODO Handle deleted headers by fetching existing and diffing
        headers.forEach { entry ->
            upsertHeader(page.id, entry.key, entry.value)
        }
        val links: Set<URI> = getLinks(doc, link, robotsTxt)
        links.forEach { l ->
            pageDbManager.upsert(l, PageSource.SCRAPE)
        }
        return ScrapedPage(
            link,
            headers,
            doc.title(),
            links,
            doc.body().html(),
            page.id,
        )
    }

    private fun fetchDocument(link: URI): Document? {
        try {
            return Jsoup.connect(link.toString()).get()
        } catch (e: Throwable) {
            logger.error("Exception calling link $link", e)
            pageDbManager.updateScrapedAt(link.toString())
        }
        return null
    }

    private fun getHeaders(doc: Document): EnumMap<OGHeader, String> {
        val headers: Elements = doc.select("meta")
        val headersMap: EnumMap<OGHeader, String> = EnumMap(OGHeader::class.java)
        for (header in headers) {
            val content = header.attr("content")
            when (header.attr("property")) {
                OGHeader.OG_TITLE.label -> headersMap[OGHeader.OG_TITLE] = content
                OGHeader.OG_DESCRIPTION.label -> headersMap[OGHeader.OG_DESCRIPTION] = content
                OGHeader.OG_IMAGE.label -> headersMap[OGHeader.OG_IMAGE] = content
                OGHeader.OG_TYPE.label -> headersMap[OGHeader.OG_TYPE] = content
                OGHeader.OG_URL.label -> headersMap[OGHeader.OG_URL] = content
                OGHeader.OG_SITE_NAME.label -> headersMap[OGHeader.OG_SITE_NAME] = content
            }
        }
        return headersMap
    }

    private fun getLinks(doc: Document, requestLink: URI, robotsTxt: SimpleRobotRules): Set<URI> {
        val links = doc.body()
            .getElementsByTag("a")
            .stream()
            .map { l ->
                try {
                    URI(l.attr("href").trim()).normalize()
                } catch (e: URISyntaxException) {
                    logger.error("Skipping Invalid URI in page ${l.attr("href")}", e)
                    null
                }
            }
            .distinct()
            .toList()
            .filterNotNull()

        return filterLinks(links, requestLink, robotsTxt)
    }

    private fun filterLinks(links: List<URI>, requestLink: URI, robotsTxt: SimpleRobotRules): Set<URI> {
        val validLinks = mutableSetOf<URI>()
        for (link in links) {
            if (!shouldProcessLink(link, requestLink, robotsTxt)) {
                continue
            }

            var path = "/"
            if (link.path != null) {
                path = link.path
            }
            val validLink: URI
            try {
                validLink = URI("${requestLink.scheme}://${requestLink.host}$path")
            } catch (e: Exception) {
                logger.error("Invalid URI $requestLink", e)
                continue
            }

            if (validLinks.contains(validLink)) {
                continue
            }

            validLinks.add(validLink)
        }
        return validLinks
    }

    private fun shouldProcessLink(
        link: URI,
        requestLink: URI,
        robotsTxt: SimpleRobotRules
    ): Boolean {
        return isLinkAllowedByRobotsTxt(link, robotsTxt) &&
                isHostValid(link, requestLink) &&
                isPathValid(link)
    }

    private fun isLinkAllowedByRobotsTxt(link: URI, robotsTxt: SimpleRobotRules): Boolean {
        if (link.path == null) {
            return false
        }

        for (robotRule in robotsTxt.robotRules) {
            if (robotRule.isAllow && link.path.startsWith(robotRule.prefix)) {
                return true
            }
            if (!robotRule.isAllow && link.path.startsWith(robotRule.prefix)) {
                return false;
            }
        }
        return true
    }

    private fun isHostValid(link: URI, requestLink: URI): Boolean {
        return link.host != null || !requestLink.host.equals(link.host)
    }

    private fun isPathValid(link: URI): Boolean {
        return link.path != null &&
                !(link.path.endsWith("jpg") ||
                        link.path.endsWith("pdf") ||
                        link.path.endsWith("xml") ||
                        link.path.contains(" "))
    }

    private fun upsertHeader(pageId: Long, headerType: OGHeader, value: String) {
        val pageHeader = pageHeaderDbManager.get(pageId, headerType)
        if (pageHeader == null) {
            pageHeaderDbManager.insert(pageId, headerType, value)
        } else if (pageHeader.value != value) {
            pageHeaderDbManager.update(pageId, headerType, value)
        }

    }

}
