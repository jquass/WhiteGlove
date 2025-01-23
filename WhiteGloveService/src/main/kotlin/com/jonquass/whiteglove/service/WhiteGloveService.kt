package com.jonquass.whiteglove.service

import com.google.inject.Guice
import com.google.inject.Injector
import com.jonquass.whiteglove.core.jdbi.GuiceJdbi
import com.jonquass.whiteglove.data.config.WhiteGloveDataModule
import com.jonquass.whiteglove.service.config.WhiteGloveConfiguration
import com.jonquass.whiteglove.service.resources.WebResource
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
        val whiteGloveDataModule = WhiteGloveDataModule()
        injector = Guice.createInjector(
            whiteGloveDataModule, { binder ->
                binder.bind(DataSource::class.java)
                    .annotatedWith(GuiceJdbi::class.java)
                    .toInstance(whiteGloveDataModule.getDataSource())

            }
        )
        injector.injectMembers(this)
    }

    override fun run(config: WhiteGloveConfiguration?, env: Environment?) {
        env?.jersey()?.register(injector.getInstance(WebResource::class.java))
    }

}