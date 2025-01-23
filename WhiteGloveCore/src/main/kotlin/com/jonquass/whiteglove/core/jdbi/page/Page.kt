package com.jonquass.whiteglove.core.jdbi.page

import java.net.URI
import java.time.Instant

data class Page(
    val id: Long,
    val link: URI,
    val title: String? = null,
    val html: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
    val scrapedAt: Instant?,
)
