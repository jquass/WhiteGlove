package com.jonquass.whiteglove.core.web.web

import com.jonquass.whiteglove.core.web.response.ResponseType

data class Response(
    val status: ResponseType,
    val content: Page? = null,
)
