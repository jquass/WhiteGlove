package com.jonquass.whiteglove.data.jdbi

import com.jonquass.whiteglove.core.jdbi.Page
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.net.URI
import java.sql.ResultSet

class PageMapper : RowMapper<Page> {
    override fun map(rs: ResultSet?, ctx: StatementContext?): Page {
        var link = ""
        if (rs?.getString("link") != null) {
            link = rs.getString("link")
        }

        var id = 0L
        if (rs?.getLong("id") != null) {
            id = rs.getLong("id")
        }

        return Page(id, URI(link))
    }
}