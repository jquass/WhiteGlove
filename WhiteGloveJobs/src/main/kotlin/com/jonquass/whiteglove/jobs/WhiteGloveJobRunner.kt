package com.jonquass.whiteglove.jobs

import com.google.inject.Guice
import com.jonquass.whiteglove.core.jdbi.GuiceJdbi
import com.jonquass.whiteglove.data.config.WhiteGloveDataModule
import com.jonquass.whiteglove.jobs.web.WebCrawlerJob
import org.quartz.*
import org.quartz.JobBuilder.newJob
import org.quartz.SimpleScheduleBuilder.simpleSchedule
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.impl.StdSchedulerFactory
import javax.sql.DataSource


class WhiteGloveJobRunner {

    // TODO implement yaml file job configuration
    // TODO implement args for job options
    // TODO implement the ability to run other jobs

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var scheduler: Scheduler? = null
            Runtime.getRuntime().addShutdownHook(object : Thread() {
                override fun run() {
                    try {
                        if (scheduler != null) {
                            scheduler?.shutdown()
                        }
                    } catch (se: SchedulerException) {
                        se.printStackTrace()
                    }
                }
            })

            val whiteGloveDataModule = WhiteGloveDataModule()
            val injector = Guice.createInjector(
                whiteGloveDataModule, { binder ->
                    binder.bind(DataSource::class.java)
                        .annotatedWith(GuiceJdbi::class.java)
                        .toInstance(whiteGloveDataModule.getDataSource())
                }
            )
            injector.injectMembers(this)

            try {
                scheduler = StdSchedulerFactory.getDefaultScheduler()
                scheduler.setJobFactory(GuiceJobFactory(injector))
                scheduler?.start()
                val job: JobDetail = jobDetail(WebCrawlerJob::class.java)
                val trigger: Trigger = cronDetailOneOff()
                scheduler?.scheduleJob(job, trigger)
            } catch (se: SchedulerException) {
                se.printStackTrace()
            }
        }

        private fun jobDetail(clazz: Class<out Job>): JobDetail {
            return newJob(clazz)
                .withIdentity(JobKey.jobKey(clazz.getName()))
                .usingJobData("domain", "https://www.democracynow.org")
                .build()
        }

        private fun cronDetailOneOff(): Trigger {
            return newTrigger()
                .withIdentity("one-off")
                .startNow()
                .withSchedule(
                    simpleSchedule().withRepeatCount(0)
                )
                .build()
        }
    }


}