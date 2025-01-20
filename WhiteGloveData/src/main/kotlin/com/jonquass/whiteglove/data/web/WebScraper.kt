package com.jonquass.whiteglove.data.web

import com.jonquass.whiteglove.core.web.web.Header
import com.jonquass.whiteglove.core.web.web.Page
import com.jonquass.whiteglove.core.web.web.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.net.URI
import java.util.*

class WebScraper {

    fun scrapeUrl(request: Request): Page {
        val url = URI(request.url);
        val doc: Document = Jsoup.connect(url.toString()).get()
        val headers: EnumMap<Header, String> = getHeaders(doc)
        val links: List<URI> = getLinks(doc)
        return Page(
            url.host,
            url.scheme,
            url.path,
            url.query,
            url,
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

    private fun getLinks(doc: Document): List<URI> {
        return doc.body()
            .getElementsByTag("a")
            .stream()
            .map { l -> URI(l.attr("href")) }
            .distinct()
            .toList()
    }

}
