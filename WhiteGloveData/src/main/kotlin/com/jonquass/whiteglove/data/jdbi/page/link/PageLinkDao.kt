package com.jonquass.whiteglove.data.jdbi.page.link

import com.jonquass.whiteglove.core.jdbi.page.PageLink
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface PageLinkDao {

    @GetGeneratedKeys
    @SqlUpdate(
        """
            INSERT INTO page_links (page_id, link, created_at) 
            VALUES (:page_id, :link, :activity_at)
        """
    )
    fun insert(
        @Bind("page_id") pageId: Long,
        @Bind("link") type: String,
        @Bind("activity_at") activityAt: Long,
    ): Long

    @SqlQuery("SELECT * FROM page_links WHERE id = :id")
    fun get(@Bind("id") id: Long): PageLink?

    @SqlQuery("SELECT * FROM page_links WHERE page_id = :page_id AND link = :link")
    fun get(@Bind("page_id") pageId: Long, @Bind("link") link: String): PageLink?

}
