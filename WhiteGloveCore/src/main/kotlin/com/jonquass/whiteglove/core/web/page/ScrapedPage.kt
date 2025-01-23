package com.jonquass.whiteglove.core.web.page

import java.net.URI

class ScrapedPage(
    val url: URI,
    val headers: Map<OGHeader, String>,
    val title: String?,
    val links: Set<URI>,
    val html: String,
    val pageId: Long?,
)
