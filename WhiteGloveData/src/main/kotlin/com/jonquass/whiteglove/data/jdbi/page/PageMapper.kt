package com.jonquass.whiteglove.data.jdbi.page

import com.jonquass.whiteglove.core.jdbi.page.Page
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.net.URI
import java.sql.ResultSet
import java.time.Instant

class PageMapper : RowMapper<Page> {
    override fun map(rs: ResultSet?, ctx: StatementContext?): Page {
        if (rs !is ResultSet) {
            throw RuntimeException()
        }

        return Page(
            rs.getLong("id"),
            URI(rs.getString("link")),
            rs.getString("title"),
            rs.getString("html"),
            Instant.ofEpochMilli(rs.getLong("created_at")),
            Instant.ofEpochMilli(rs.getLong("updated_at")),
            Instant.ofEpochMilli(rs.getLong("scraped_at"))
        )
    }
}
