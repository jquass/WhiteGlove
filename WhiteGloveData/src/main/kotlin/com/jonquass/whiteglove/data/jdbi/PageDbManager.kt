package com.jonquass.whiteglove.data.jdbi

import com.google.inject.Inject
import com.google.inject.Singleton
import com.jonquass.whiteglove.core.jdbi.GuiceJdbi
import org.jdbi.v3.core.Jdbi
import java.time.Instant

@Singleton
class PageDbManager @Inject constructor(@GuiceJdbi val jdbi: Jdbi) {

    fun insert(link: String): Long {
        return jdbi.withExtension<Long, PageDao, Exception>(PageDao::class.java) { dao ->
            dao.insert(link, Instant.now().toEpochMilli())
        }
    }

}