package com.jonquass.whiteglove.data.web

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jonquass.whiteglove.core.web.Header
import com.jonquass.whiteglove.core.web.Page
import com.jonquass.whiteglove.core.web.Request
import com.jonquass.whiteglove.data.jdbi.PageDbManager
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.net.URI
import java.util.*

@Singleton
class WebScraper @Inject constructor(private var pageDbManager: PageDbManager) {

    fun scrapeLink(request: Request): Page {

        val id = pageDbManager.insert(request.url)

        println("ID $id")

        val url = URI(request.url);
        return scrapeLink(url)
    }

    private fun scrapeLink(link: URI): Page {
        val doc: Document = Jsoup.connect(link.toString()).get()
        val headers: EnumMap<Header, String> = getHeaders(doc)
        val links: Set<URI> = getLinks(doc, link)
        return Page(
            link,
            headers,
            doc.title(),
            links,
            doc.body().html()
        )
    }

    private fun getHeaders(doc: Document): EnumMap<Header, String> {
        val headers: Elements = doc.select("meta")
        val headersMap: EnumMap<Header, String> = EnumMap(Header::class.java)
        for (header in headers) {
            val content = header.attr("content")
            when (header.attr("property")) {
                Header.OG_TITLE.attribute -> headersMap[Header.OG_TITLE] = content
                Header.OG_DESCRIPTION.attribute -> headersMap[Header.OG_DESCRIPTION] = content
                Header.OG_IMAGE.attribute -> headersMap[Header.OG_IMAGE] = content
                Header.OG_TYPE.attribute -> headersMap[Header.OG_TYPE] = content
                Header.OG_URL.attribute -> headersMap[Header.OG_URL] = content
                Header.OG_SITE_NAME.attribute -> headersMap[Header.OG_SITE_NAME] = content
            }
        }
        return headersMap
    }

    private fun getLinks(doc: Document, requestLink: URI): Set<URI> {
        val links = doc.body()
            .getElementsByTag("a")
            .stream()
            .map { l -> URI(l.attr("href")) }
            .distinct()
            .toList()

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
        return link.host != null && !requestLink.host.equals(link.host)
    }

}
