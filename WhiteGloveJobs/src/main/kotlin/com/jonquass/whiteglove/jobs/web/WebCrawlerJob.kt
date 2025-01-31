package com.jonquass.whiteglove.jobs.web

import com.google.inject.Inject
import com.jonquass.whiteglove.data.web.WebCrawler
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import java.net.URI

class WebCrawlerJob @Inject constructor(private var webCrawler: WebCrawler) : Job {

    private var logger = LoggerFactory.getLogger(WebCrawlerJob::class.java)

    override fun execute(context: JobExecutionContext?) {

        val domain = context?.jobDetail?.jobDataMap?.get("domain") as String

        logger.info("Crawling Domain $domain")

        webCrawler.crawlDomain(URI(domain))
    }


}