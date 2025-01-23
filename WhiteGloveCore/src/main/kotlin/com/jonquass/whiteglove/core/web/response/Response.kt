package com.jonquass.whiteglove.core.web.response

import com.jonquass.whiteglove.core.web.page.ScrapedPage

data class Response(
    val status: ResponseType,
    val content: ScrapedPage? = null,
)
