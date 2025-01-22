package com.jonquass.whiteglove.core.web

import java.net.URI

class Page(
    val url: URI,
    val headers: Map<Header, String>,
    val title: String?,
    val links: Set<URI>,
    val html: String,
)
