package com.jonquass.whiteglove.data.jdbi.page.header

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jonquass.whiteglove.core.jdbi.GuiceJdbi
import com.jonquass.whiteglove.core.jdbi.page.PageHeader
import com.jonquass.whiteglove.core.web.page.OGHeader
import org.jdbi.v3.core.Jdbi
import java.time.Instant

@Singleton
class PageHeaderDbManager @Inject constructor(@GuiceJdbi val jdbi: Jdbi) {

    fun insert(pageId: Long, type: OGHeader, value: String): Long {
        return jdbi.withExtension<Long, PageHeaderDao, Exception>(PageHeaderDao::class.java) { dao ->
            dao.insert(pageId, type.label, value, Instant.now().toEpochMilli())
        }
    }

    fun get(pageId: Long, type: OGHeader): PageHeader? {
        return jdbi.withExtension<PageHeader, PageHeaderDao, Exception>(PageHeaderDao::class.java) { dao ->
            dao.get(pageId, type.label)
        }
    }

    fun update(pageId: Long, type: OGHeader, value: String): Int {
        return jdbi.withExtension<Int, PageHeaderDao, Exception>(PageHeaderDao::class.java) { dao ->
            dao.update(pageId, type.label, value, Instant.now().toEpochMilli())
        }
    }
}