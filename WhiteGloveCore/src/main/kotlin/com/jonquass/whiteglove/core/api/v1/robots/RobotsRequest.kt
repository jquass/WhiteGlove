package com.jonquass.whiteglove.core.api.v1.robots

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class RobotsRequest(
    @JsonProperty("url") val url: URI
)
