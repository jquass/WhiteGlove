package com.jonquass.whiteglove.service

import com.google.inject.Guice
import com.google.inject.Injector
import com.jonquass.whiteglove.core.jdbi.GuiceJdbi
import com.jonquass.whiteglove.data.config.WhiteGloveDataModule
import com.jonquass.whiteglove.service.config.WhiteGloveConfiguration
import com.jonquass.whiteglove.service.resources.WebResource
import com.mysql.cj.jdbc.MysqlDataSource
import io.dropwizard.core.Application
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment
import javax.sql.DataSource


class WhiteGloveService : Application<WhiteGloveConfiguration>() {

    private lateinit var injector: Injector

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            WhiteGloveService().run(*args)
        }
    }

    override fun initialize(bootstrap: Bootstrap<WhiteGloveConfiguration>?) {
        injector = Guice.createInjector(
            WhiteGloveDataModule(), { binder ->
                binder.bind(DataSource::class.java)
                    .annotatedWith(GuiceJdbi::class.java)
                    .toInstance(getDataSource())

            }
        )
        injector.injectMembers(this)
    }

    private fun getDataSource(): MysqlDataSource {
        val mysqlDS = MysqlDataSource()
        mysqlDS.databaseName = System.getenv("MYSQL_DATABASE_NAME")
        mysqlDS.serverName = System.getenv("MYSQL_SERVER_NAME")
        mysqlDS.port = System.getenv("MYSQL_PORT").toInt()
        mysqlDS.user = System.getenv("MYSQL_USER")
        mysqlDS.password = System.getenv("MYSQL_PASSWORD")
        return mysqlDS
    }

    override fun run(config: WhiteGloveConfiguration?, env: Environment?) {
        env?.jersey()?.register(injector.getInstance(WebResource::class.java))
    }

}