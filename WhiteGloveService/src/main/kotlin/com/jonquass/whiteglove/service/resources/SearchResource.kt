package com.jonquass.whiteglove.service.resources

import com.google.inject.Inject
import com.jonquass.whiteglove.core.api.v1.response.Response
import com.jonquass.whiteglove.core.api.v1.response.ResponseType
import com.jonquass.whiteglove.core.api.v1.search.SearchRequest
import com.jonquass.whiteglove.data.jdbi.page.PageDbManager
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/search")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class SearchResource @Inject constructor(private var pageDbManager: PageDbManager) {

    @POST
    fun search(searchRequest: SearchRequest): Response<List<String>> {
        val pages = pageDbManager.searchInNaturalLanguageMode(searchRequest.search, searchRequest.limit)
        return Response(ResponseType.SUCCESS, pages.map { page -> page.link.toString() }.toList())
    }

}