package com.jonquass.whiteglove.data.jdbi.page

import com.jonquass.whiteglove.core.jdbi.page.Page
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface PageDao {

    @GetGeneratedKeys
    @SqlUpdate(
        """
        INSERT INTO pages (link, host, created_at, updated_at, scraped_at) 
        VALUES (:link, :host, :activity_at, :activity_at, :scraped_at)
        """
    )
    fun insert(
        @Bind("link") link: String,
        @Bind("host") host: String,
        @Bind("activity_at") activityAt: Long,
        @Bind("scraped_at") scrapedAt: Long?,
    ): Long

    @SqlQuery("SELECT * FROM pages WHERE id = :id")
    fun get(@Bind("id") id: Long): Page?

    @SqlQuery("SELECT * FROM pages WHERE link = :link")
    fun get(@Bind("link") link: String): Page?

    @SqlUpdate(
        """
        UPDATE pages
        SET title = :title, 
            html = :html, 
            updated_at = :activity_at,
            scraped_at = :activity_at
        WHERE id = :id
        """
    )
    fun update(
        @Bind("id") id: Long,
        @Bind("title") title: String,
        @Bind("html") html: String,
        @Bind("activity_at") activityAt: Long,
    )

    @SqlUpdate(
        """
        UPDATE pages
        SET scraped_at = :scraped_at
        WHERE id = :id
        """
    )
    fun updateScrapedAt(
        @Bind("id") id: Long,
        @Bind("scraped_at") scrapedAt: Long,
    )

    @SqlUpdate(
        """
        UPDATE pages
        SET scraped_at = :scraped_at
        WHERE link = :link
        """
    )
    fun updateScrapedAt(
        @Bind("link") link: String,
        @Bind("scraped_at") scrapedAt: Long,
    )

    @SqlQuery(
        """
        SELECT * FROM pages 
        WHERE host = :host 
            AND (scraped_at IS NULL OR scraped_at < :scraped_at)
        LIMIT :limit
        """
    )
    fun list(
        @Bind("host") host: String,
        @Bind("scraped_at") scrapedAt: Long,
        @Bind("limit") limit: Int,
    ): List<Page>
}
