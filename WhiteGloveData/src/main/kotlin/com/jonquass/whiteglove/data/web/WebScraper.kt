package com.jonquass.whiteglove.data.web

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jonquass.whiteglove.core.jdbi.page.Page
import com.jonquass.whiteglove.core.jdbi.page.PageLink
import com.jonquass.whiteglove.core.web.page.OGHeader
import com.jonquass.whiteglove.core.web.page.PageRequest
import com.jonquass.whiteglove.core.web.page.ScrapedPage
import com.jonquass.whiteglove.data.jdbi.page.PageDbManager
import com.jonquass.whiteglove.data.jdbi.page.header.PageHeaderDbManager
import com.jonquass.whiteglove.data.jdbi.page.link.PageLinkDbManager
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.net.URI
import java.net.URISyntaxException
import java.util.*

@Singleton
class WebScraper @Inject constructor(
    private var pageDbManager: PageDbManager,
    private var pageHeaderDbManager: PageHeaderDbManager,
    private var pageLinkDbManager: PageLinkDbManager,
) {

    fun scrapeLink(pageRequest: PageRequest): ScrapedPage? {
        return scrapeLink(pageRequest.url)
    }

    fun scrapeLink(link: URI): ScrapedPage? {
        val normalizedLink = URI("${link.scheme}://${link.host}${link.path}").normalize()
        val doc: Document
        try {
            doc = Jsoup.connect(normalizedLink.toString()).get()
        } catch (e: Throwable) {
            println("Exception calling link $normalizedLink $e")
            pageDbManager.updateScrapedAt(normalizedLink.toString())
            return null
        }

        val newPage = pageDbManager.upsert(normalizedLink, doc)
        val headers: EnumMap<OGHeader, String> = getHeaders(doc)
        headers.forEach { entry ->
            upsertHeaders(newPage.id, entry.key, entry.value)
        }

        val links: Set<URI> = getLinks(doc, normalizedLink)
        links.forEach { l ->
            upsertLinks(l, newPage.id)
        }

        return ScrapedPage(
            normalizedLink,
            headers,
            doc.title(),
            links,
            doc.body().html(),
            newPage.id,
        )
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

    private fun getLinks(doc: Document, requestLink: URI): Set<URI> {
        val links = doc.body()
            .getElementsByTag("a")
            .stream()
            .map { l ->
                try {
                    URI(l.attr("href").trim()).normalize()
                } catch (e: URISyntaxException) {
                    println("Invalid URI in page ${l.attr("href")} $e")
                    null
                }
            }
            .distinct()
            .toList()
            .filterNotNull()

        return filterLinks(links, requestLink)
    }

    private fun filterLinks(links: List<URI>, requestLink: URI): Set<URI> {
        val validLinks = mutableSetOf<URI>()
        for (link in links) {
            if (shouldSkipLink(link, requestLink)) {
                continue
            }

            var path = "/"
            if (link.path != null) {
                path = link.path
            }
            val validLink = URI("${requestLink.scheme}://${requestLink.host}$path")
            if (validLinks.contains(validLink)) {
                continue;
            }

            validLinks.add(validLink)
        }
        return validLinks
    }

    private fun shouldSkipLink(
        link: URI,
        requestLink: URI
    ): Boolean {
        return !isHostValid(link, requestLink) || !isPathValid(link)
    }

    private fun isHostValid(link: URI, requestLink: URI): Boolean {
        return link.host != null || !requestLink.host.equals(link.host)
    }

    private fun isPathValid(link: URI): Boolean {
        return link.path == null ||
                !(link.path.endsWith("jpg") ||
                        link.path.endsWith("pdf") ||
                        link.path.endsWith("xml") ||
                        link.path.contains(" "))
    }


    private fun upsertHeaders(pageId: Long, headerType: OGHeader, value: String) {
        val pageHeader = pageHeaderDbManager.get(pageId, headerType)
        if (pageHeader == null) {
            pageHeaderDbManager.insert(pageId, headerType, value)
        } else if (pageHeader.value != value) {
            pageHeaderDbManager.update(pageId, headerType, value)
        }
        //TODO Delete old headers
    }

    private fun upsertLinks(link: URI, pageId: Long) {
        val pageLink = pageLinkDbManager.get(pageId, link)
        if (pageLink !is PageLink) {
            pageLinkDbManager.insert(pageId, link)
        }
        //TODO Delete old pageLinks

        val page = pageDbManager.get(link)
        if (page !is Page) {
            pageDbManager.insert(link, false)
        }
    }

}
