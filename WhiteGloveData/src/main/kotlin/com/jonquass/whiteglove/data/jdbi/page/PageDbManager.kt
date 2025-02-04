package com.jonquass.whiteglove.data.jdbi.page

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jonquass.whiteglove.core.jdbi.GuiceJdbi
import com.jonquass.whiteglove.core.jdbi.page.Page
import com.jonquass.whiteglove.core.web.page.PageSource
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.jsoup.nodes.Document
import java.net.URI
import java.time.Instant

@Singleton
class PageDbManager @Inject constructor(@GuiceJdbi val jdbi: Jdbi) {

    @Transaction
    fun upsert(link: URI, doc: Document, source: PageSource): Page {
        var page = get(link)
        if (page == null) {
            val id = insert(link, doc, true, source)
            page = get(id)
        } else if (!page.html.equals(doc.body().html()) || !page.title.equals(doc.title())) {
            update(page.id, doc.title(), doc.html())
            page = get(page.id)
        } else {
            updateScrapedAt(page.id)
            page = get(page.id)
        }

        return page!!
    }

    @Transaction
    fun upsert(link: URI, source: PageSource): Page {
        var page = get(link)
        if (page == null) {
            val id = insert(link, null, false, source)
            page = get(id)
        }
        return page!!
    }

    private fun insert(link: URI, doc: Document?, scraped: Boolean, source: PageSource): Long {
        val activityAt: Long = Instant.now().toEpochMilli()
        var scrapedAt: Long? = null
        if (scraped) {
            scrapedAt = activityAt
        }
        return jdbi.withExtension<Long, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.insert(link.toString(), doc?.title(), doc?.html(), link.host, activityAt, scrapedAt, source.name)
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

    private fun update(id: Long, title: String, html: String): Int {
        return jdbi.withExtension<Int, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.update(id, title, html, Instant.now().toEpochMilli())
        }
    }

    private fun updateScrapedAt(id: Long): Int {
        return jdbi.withExtension<Int, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.updateScrapedAt(id, Instant.now().toEpochMilli())
        }
    }

    fun updateScrapedAt(link: String): Int {
        return jdbi.withExtension<Int, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.updateScrapedAt(link, Instant.now().toEpochMilli())
        }
    }

    fun listForCrawling(host: String, limit: Int): List<Page> {
        return jdbi.withExtension<List<Page>, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.list(host, Instant.now().minusSeconds(60 * 60 * 24 * 7).toEpochMilli(), limit)
        }
    }

    fun searchInNaturalLanguageMode(search: String, limit: Int): List<Page> {
        return jdbi.withExtension<List<Page>, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.searchInNaturalLanguageMode(search, limit)
        }
    }
}