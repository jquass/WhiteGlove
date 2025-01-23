package com.jonquass.whiteglove.data.jdbi.page

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jonquass.whiteglove.core.jdbi.GuiceJdbi
import com.jonquass.whiteglove.core.jdbi.page.Page
import org.jdbi.v3.core.Jdbi
import org.jsoup.nodes.Document
import java.net.URI
import java.time.Instant

@Singleton
class PageDbManager @Inject constructor(@GuiceJdbi val jdbi: Jdbi) {

    fun upsert(link: URI, doc: Document): Page {
        var page = get(link)
        if (page == null) {
            val id = insert(link, true)
            page = get(id)
        } else if (!page.html.equals(doc.body().html()) || !page.title.equals(doc.title())) {
            page = update(page.id, doc.title(), doc.html())
        } else {
            page = updateScrapedAt(page.id)
        }
        if (page == null) {
            throw RuntimeException()
        }

        return page
    }

    fun insert(link: URI, scraped: Boolean): Long {
        val activityAt: Long = Instant.now().toEpochMilli()
        var scrapedAt: Long? = null
        if (scraped) {
            scrapedAt = activityAt
        }
        return jdbi.withExtension<Long, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.insert(link.toString(), link.host, activityAt, scrapedAt)
        }
    }

    fun get(link: URI): Page? {
        return jdbi.withExtension<Page, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.get(link.toString())
        }
    }

    fun get(id: Long): Page? {
        return jdbi.withExtension<Page, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.get(id)
        }
    }

    private fun update(id: Long, title: String, html: String): Page {
        return jdbi.withExtension<Page, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.update(id, title, html, Instant.now().toEpochMilli())
            dao.get(id)
        }
    }

    private fun updateScrapedAt(id: Long): Page {
        return jdbi.withExtension<Page, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.updateScrapedAt(id, Instant.now().toEpochMilli())
            dao.get(id)
        }
    }

    fun updateScrapedAt(link: String): Page {
        return jdbi.withExtension<Page, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.updateScrapedAt(link, Instant.now().toEpochMilli())
            dao.get(link)
        }
    }

    fun listForCrawling(host: String, limit: Int): List<Page> {
        return jdbi.withExtension<List<Page>, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.list(host, Instant.now().minusSeconds(60 * 60 * 24).toEpochMilli(), limit)
        }
    }

}