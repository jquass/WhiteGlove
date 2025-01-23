package com.jonquass.whiteglove.data.jdbi.page.header

import com.jonquass.whiteglove.core.jdbi.page.PageHeader
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.time.Instant

class PageHeaderMapper : RowMapper<PageHeader> {
    override fun map(rs: ResultSet?, ctx: StatementContext?): PageHeader {
        if (rs !is ResultSet) {
            throw RuntimeException()
        }

        return PageHeader(
            rs.getLong("id"),
            rs.getLong("page_id"),
            rs.getString("type"),
            rs.getString("value"),
            Instant.ofEpochMilli(rs.getLong("created_at")),
            Instant.ofEpochMilli(rs.getLong("updated_at"))
        )
    }

}
