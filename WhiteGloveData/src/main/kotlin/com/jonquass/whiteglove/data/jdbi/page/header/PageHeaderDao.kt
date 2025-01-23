package com.jonquass.whiteglove.data.jdbi.page.header

import com.jonquass.whiteglove.core.jdbi.page.PageHeader
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface PageHeaderDao {

    @GetGeneratedKeys
    @SqlUpdate(
        """
            INSERT INTO page_headers (page_id, type, value, created_at, updated_at) 
            VALUES (:page_id, :type, :value, :activity_at, :activity_at)
        """
    )
    fun insert(
        @Bind("page_id") pageId: Long,
        @Bind("type") type: String,
        @Bind("value") value: String,
        @Bind("activity_at") activityAt: Long,
    ): Long

    @SqlQuery("SELECT * FROM page_headers WHERE id = :id")
    fun get(@Bind("id") id: Long): PageHeader?

    @SqlQuery(
        """
            SELECT * FROM page_headers 
            WHERE page_id = :page_id 
                AND type = :type
        """
    )
    fun get(@Bind("page_id") id: Long, @Bind("type") type: String): PageHeader?

    @SqlUpdate(
        """
            UPDATE page_headers
            SET value = :value,
                updated_at = :updated_at
            WHERE page_id = :page_id,
                AND type = :type
        """
    )
    fun update(
        @Bind("page_id") pageId: Long,
        @Bind("type") type: String,
        @Bind("value") value: String,
        @Bind("updated_at") updatedAt: Long,
    ): Int
}