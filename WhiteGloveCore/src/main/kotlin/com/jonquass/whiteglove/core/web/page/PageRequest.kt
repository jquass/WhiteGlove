package com.jonquass.whiteglove.core.web.page

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class PageRequest(
    @JsonProperty("url") val url: URI,
    @JsonProperty("limit") val limit: Int?,
)
