package com.jonquass.whiteglove.service.resources

import com.google.inject.Inject
import com.jonquass.whiteglove.core.api.v1.response.Response
import com.jonquass.whiteglove.core.api.v1.response.ResponseType
import com.jonquass.whiteglove.core.api.v1.robots.RobotsRequest
import com.jonquass.whiteglove.data.web.RobotsTxtClient
import crawlercommons.robots.SimpleRobotRules
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/robots")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class RobotsResource @Inject constructor(private var robotsTxtClient: RobotsTxtClient) {

    @POST
    fun robots(robotsRequest: RobotsRequest): Response<SimpleRobotRules> {
        val rules = robotsTxtClient.fetchRobotsTxt(robotsRequest)
        return Response(ResponseType.SUCCESS, rules)
    }
}