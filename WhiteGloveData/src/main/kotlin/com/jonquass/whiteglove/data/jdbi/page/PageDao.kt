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
        INSERT INTO pages (link, title, html, host, created_at, updated_at, scraped_at, source) 
        VALUES (:link, :title, :html, :host, :activity_at, :activity_at, :scraped_at, :source)
        """
    )
    fun insert(
        @Bind("link") link: String,
        @Bind("title") title: String?,
        @Bind("html") html: String?,
        @Bind("host") host: String,
        @Bind("activity_at") activityAt: Long,
        @Bind("scraped_at") scrapedAt: Long?,
        @Bind("source") source: String,
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
    ): Int

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
    ): Int

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
    ): Int

    @SqlQuery(
        """
        SELECT * FROM pages 
        WHERE host = :host 
            AND (scraped_at IS NULL OR scraped_at < :scraped_at)
        ORDER BY created_at DESC
        LIMIT :limit
        """
    )
    fun list(
        @Bind("host") host: String,
        @Bind("scraped_at") scrapedAt: Long,
        @Bind("limit") limit: Int,
    ): List<Page>

    @SqlQuery(
        """
        SELECT * 
        FROM pages p
            WHERE MATCH(html) AGAINST (:search IN NATURAL LANGUAGE MODE)
            AND scraped_at IS NOT NULL
            LIMIT :limit
        """
    )
    fun searchInNaturalLanguageMode(
        @Bind("search") search: String,
        @Bind("limit") limit: Int,
    ): List<Page>

    @SqlQuery(
        """
        SELECT * 
        FROM pages p
            WHERE MATCH(html) AGAINST (:search IN BOOLEAN MODE)
            AND scraped_at IS NOT NULL
            LIMIT :limit
        """
    )
    fun searchInBooleanMode(
        @Bind("search") search: String,
        @Bind("limit") limit: Int,
    ): List<Page>
}
