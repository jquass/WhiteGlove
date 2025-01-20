package com.jonquass.whiteglove.core.web.response

import com.jonquass.whiteglove.core.web.Page

data class Response(
    val status: ResponseType,
    val content: Page? = null,
)
