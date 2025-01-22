package com.jonquass.whiteglove.data.jdbi

import com.jonquass.whiteglove.core.jdbi.Page
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface PageDao {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO page (link, created_at) VALUES (:link, :created_at)")
    fun insert(@Bind("link") link: String, @Bind("created_at") createdAt: Long): Long

    @SqlQuery("SELECT * FROM page WHERE id = :id")
    fun getPage(id: Long): Page?

    @SqlQuery("SELECT * FROM page WHERE link = :link")
    fun getPage(link: String): Page?

}
