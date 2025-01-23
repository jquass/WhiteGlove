package com.jonquass.whiteglove.data.jdbi.page.link

import com.google.inject.Inject
import com.jonquass.whiteglove.core.jdbi.GuiceJdbi
import com.jonquass.whiteglove.core.jdbi.page.PageLink
import org.jdbi.v3.core.Jdbi
import java.net.URI
import java.time.Instant

class PageLinkDbManager @Inject constructor(@GuiceJdbi val jdbi: Jdbi) {

    fun insert(pageId: Long, link: URI): Long {
        return jdbi.withExtension<Long, PageLinkDao, Exception>(PageLinkDao::class.java) { dao ->
            dao.insert(pageId, link.toString(), Instant.now().toEpochMilli())
        }
    }

    fun get(pageId: Long, link: URI): PageLink? {
        return jdbi.withExtension<PageLink, PageLinkDao, Exception>(PageLinkDao::class.java) { dao ->
            dao.get(pageId, link.toString())
        }
    }

}