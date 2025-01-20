package com.jonquass.whiteglove.service

import com.jonquass.whiteglove.service.config.WhiteGloveConfiguration
import io.dropwizard.core.Application
import io.dropwizard.core.setup.Environment
import com.jonquass.whiteglove.service.resources.WebScrapeResource

class WhiteGloveApplication : Application<WhiteGloveConfiguration>() {

    override fun run(config: WhiteGloveConfiguration?, env: Environment?) {

        env?.jersey()?.register(WebScrapeResource())
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            WhiteGloveApplication().run(*args)
        }
    }
}