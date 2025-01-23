package com.jonquass.whiteglove.data.config

import com.jonquass.whiteglove.core.jdbi.GuiceJdbi
import com.jonquass.whiteglove.data.jdbi.page.PageMapper
import com.jonquass.whiteglove.data.jdbi.page.header.PageHeaderMapper
import com.jonquass.whiteglove.data.jdbi.page.link.PageLinkMapper
import com.mysql.cj.jdbc.MysqlDataSource
import org.jdbi.v3.guice.AbstractJdbiDefinitionModule
import org.jdbi.v3.sqlobject.SqlObjectPlugin

class WhiteGloveDataModule : AbstractJdbiDefinitionModule(GuiceJdbi::class.java) {

    override fun configureJdbi() {
        bindPlugin().to(SqlObjectPlugin::class.java)

        bindRowMapper().to(PageMapper::class.java)
        bindRowMapper().to(PageHeaderMapper::class.java)
        bindRowMapper().to(PageLinkMapper::class.java)
    }

    fun getDataSource(): MysqlDataSource {
        val mysqlDS = MysqlDataSource()
        mysqlDS.databaseName = System.getenv("MYSQL_DATABASE_NAME")
        mysqlDS.serverName = System.getenv("MYSQL_SERVER_NAME")
        mysqlDS.port = System.getenv("MYSQL_PORT").toInt()
        mysqlDS.user = System.getenv("MYSQL_USER")
        mysqlDS.password = System.getenv("MYSQL_PASSWORD")
        return mysqlDS
    }

}
