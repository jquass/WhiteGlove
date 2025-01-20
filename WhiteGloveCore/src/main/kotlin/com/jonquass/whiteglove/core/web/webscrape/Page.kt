package com.jonquass.whiteglove.core.web.webscrape

import java.net.URI

class Page(
    val host: String,
    val scheme: String,
    val path: String,
    val query: String?,
    val url: URI,
    val headers: Map<Header, String>,
    val title: String?,
    val links: List<URI>,
    val html: String,
)
