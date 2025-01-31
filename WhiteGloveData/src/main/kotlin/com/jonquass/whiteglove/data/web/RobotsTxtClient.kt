package com.jonquass.whiteglove.data.web

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jonquass.whiteglove.core.api.v1.robots.RobotsRequest
import com.jonquass.whiteglove.data.config.WhiteGloveDataModule.Companion.AGENT_NAME
import crawlercommons.robots.SimpleRobotRules
import crawlercommons.robots.SimpleRobotRulesParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URI


@Singleton
class RobotsTxtClient @Inject constructor(
    private val robotParse: SimpleRobotRulesParser
) {

    fun fetchRobotsTxt(robotsRequest: RobotsRequest): SimpleRobotRules {
        return fetchRobotsTxt(robotsRequest.url)
    }

    fun fetchRobotsTxt(url: URI): SimpleRobotRules {
        val robotUrl = URI("${url.scheme}://${url.host}/robots.txt")
        val inputStream = BufferedReader(InputStreamReader(robotUrl.toURL().openStream()))
        var output = inputStream.readLine()
        var input: String
        while ((inputStream.readLine().also { input = it }) != null) {
            output += "\n"
            output += input
        }
        inputStream.close()

        val robotRules =
            robotParse.parseContent(
                robotUrl.toString(),
                output.toByteArray(),
                "text/plain",
                listOf(AGENT_NAME)
            )
        return robotRules
    }

}