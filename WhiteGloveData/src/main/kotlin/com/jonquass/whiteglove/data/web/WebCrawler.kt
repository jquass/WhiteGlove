package com.jonquass.whiteglove.data.web

import com.google.inject.Inject
import com.jonquass.whiteglove.core.web.Page
import com.jonquass.whiteglove.core.web.Request
import java.net.URI

class WebCrawler {

    @Inject
    lateinit var webScraper: WebScraper

    fun crawlUrl(request: Request) {
        val visitedLinks = mutableSetOf<URI>()
        val skippedLinks = mutableSetOf<URI>()

        // Scrape first page
        val page: Page = webScraper.scrapeLink(request)
        val requestLink = URI(request.url)
        visitedLinks.add(requestLink)

        val linksBatch = page.links

        processLinksBatch(requestLink, linksBatch, skippedLinks, visitedLinks)

        println("Visited Links $visitedLinks")
        println("Skipped Links $skippedLinks")


    }

    private fun processLinksBatch(
        requestLink: URI,
        linksBatch: List<URI>,
        skippedLinks: MutableSet<URI>,
        visitedLinks: MutableSet<URI>
    ) {
        for (link in linksBatch) {
            println("Processing link $link")
            if (skippedLinks.contains(link) || visitedLinks.contains(link)) {
                continue
            }

            if (shouldSkipLink(link, requestLink)) {
                println("Skipping Link $link")
                skippedLinks.add(link)
                continue
            }

            println("Visiting $link")
            visitedLinks.add(link)

        }
    }

    private fun shouldSkipLink(
        link: URI,
        requestLink: URI
    ): Boolean {
        return link.host != null
                && !requestLink.host.equals(link.host)
    }

}