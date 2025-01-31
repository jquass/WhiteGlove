package com.jonquass.whiteglove.core.api.v1.response

data class Response<E>(
    val status: ResponseType,
    val content: E? = null,
)
