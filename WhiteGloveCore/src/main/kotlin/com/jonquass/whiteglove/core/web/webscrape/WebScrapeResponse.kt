package com.jonquass.whiteglove.core.web.webscrape

import com.jonquass.whiteglove.core.web.response.ResponseType

data class WebScrapeResponse(
    val status: ResponseType,
    val content: Page,
)
