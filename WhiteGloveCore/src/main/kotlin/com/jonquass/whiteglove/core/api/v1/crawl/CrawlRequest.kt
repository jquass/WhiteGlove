package com.jonquass.whiteglove.core.api.v1.crawl

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class CrawlRequest(
    @JsonProperty("url") val url: URI,
    @JsonProperty("limit") val limit: Int? = 100,
)
