package com.jonquass.whiteglove.core.jdbi.page

import java.time.Instant

data class PageLink(
    val id: Long,
    val pageId: Long,
    val link: String,
    val createdAt: Instant,
)
