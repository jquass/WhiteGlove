package com.jonquass.whiteglove.data.jdbi.page.link

import com.jonquass.whiteglove.core.jdbi.page.PageLink
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.time.Instant

class PageLinkMapper : RowMapper<PageLink> {
    override fun map(rs: ResultSet?, ctx: StatementContext?): PageLink {
        if (rs !is ResultSet) {
            throw RuntimeException()
        }

        return PageLink(
            rs.getLong("id"),
            rs.getLong("page_id"),
            rs.getString("link"),
            Instant.ofEpochMilli(rs.getLong("created_at")),
        )
    }
}