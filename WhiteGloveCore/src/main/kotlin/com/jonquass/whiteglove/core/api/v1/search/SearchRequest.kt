package com.jonquass.whiteglove.core.api.v1.search

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class SearchRequest(
    @JsonProperty("search") val search: String,
    @JsonProperty("limit") val limit: Int = 1_000,
)
