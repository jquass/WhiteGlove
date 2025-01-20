package com.jonquass.whiteglove.service

import com.google.inject.Guice
import com.jonquass.whiteglove.service.config.WhiteGloveConfiguration
import com.jonquass.whiteglove.service.config.WhiteGloveServiceModule
import com.jonquass.whiteglove.service.resources.WebResource
import io.dropwizard.core.Application
import io.dropwizard.core.setup.Environment

class WhiteGloveApplication : Application<WhiteGloveConfiguration>() {

    override fun run(config: WhiteGloveConfiguration?, env: Environment?) {
        val injector = Guice.createInjector(WhiteGloveServiceModule())

        env?.jersey()?.register(injector.getInstance(WebResource::class.java))
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            WhiteGloveApplication().run(*args)
        }
    }
}