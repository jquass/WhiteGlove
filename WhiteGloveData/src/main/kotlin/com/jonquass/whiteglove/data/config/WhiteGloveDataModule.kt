package com.jonquass.whiteglove.data.config

import com.jonquass.whiteglove.core.jdbi.GuiceJdbi
import com.jonquass.whiteglove.data.jdbi.PageMapper
import org.jdbi.v3.guice.AbstractJdbiDefinitionModule
import org.jdbi.v3.sqlobject.SqlObjectPlugin

class WhiteGloveDataModule : AbstractJdbiDefinitionModule(GuiceJdbi::class.java) {

    override fun configureJdbi() {
        bindPlugin().to(SqlObjectPlugin::class.java)

        bindRowMapper().to(PageMapper::class.java)
    }

}
