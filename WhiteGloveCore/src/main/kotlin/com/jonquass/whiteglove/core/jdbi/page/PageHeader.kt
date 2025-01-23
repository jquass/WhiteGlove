package com.jonquass.whiteglove.core.jdbi.page

import java.time.Instant

data class PageHeader(
    val id: Long,
    val pageId: Long,
    val type: String,
    val value: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
