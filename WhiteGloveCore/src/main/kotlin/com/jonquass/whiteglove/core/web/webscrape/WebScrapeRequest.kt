package com.jonquass.whiteglove.core.web.webscrape

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class WebScrapeRequest(@JsonProperty("url") val url: String)
