package com.jonquass.whiteglove.jobs

import com.google.inject.Injector
import org.quartz.Job
import org.quartz.Scheduler
import org.quartz.spi.JobFactory
import org.quartz.spi.TriggerFiredBundle


class GuiceJobFactory(private val injector: Injector) : JobFactory {
    override fun newJob(bundle: TriggerFiredBundle, scheduler: Scheduler?): Job {
        return injector.getInstance(bundle.jobDetail.jobClass)
    }
}