package com.jonquass.whiteglove.data.web

import com.google.inject.Singleton
import crawlercommons.robots.SimpleRobotRules
import crawlercommons.robots.SimpleRobotRules.RobotRule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI

@Singleton
class RobotsTxtManager {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun shouldScrape(link: URI, robotsTxt: SimpleRobotRules): Boolean {
        if (robotsTxt.isAllowNone) {
            logger.info("Robots.txt is allow none $link")
            return false
        }

        if (robotsTxt.isAllowAll) {
            logger.info("Robots.txt is allow all $link")
            return true
        }

        val matchedRobotRules: List<RobotRule> = matchRules(robotsTxt, link)
        if (matchedRobotRules.isNotEmpty() && !shouldScrape(matchedRobotRules)) {
            logger.info("Skipping link due to robots.txt rules")
            return false
        }

        return true
    }

    private fun matchRules(robotsTxt: SimpleRobotRules, link: URI): List<RobotRule> {
        val matchedRules: MutableList<RobotRule> = mutableListOf()
        for (robotRule in robotsTxt.robotRules) {
            if (ruleMatchesUrl(robotRule, link)) {
                matchedRules.add(robotRule)
            }
        }
        return matchedRules
    }

    internal fun ruleMatchesUrl(robotRule: RobotRule, link: URI): Boolean {
        var regexString = robotRule.prefix!!
            .replace("/", "\\/")
            .replace("*", ".*")

        if (!regexString.endsWith("$")) {
            regexString = "$regexString.*$"
        }

        val regex = Regex(regexString)
        val path = link.path ?: ""
        val query = link.query ?: ""
        return regex.matches("$path$query")
    }

    private fun shouldScrape(matchedRules: List<RobotRule>): Boolean {
        if (matchedRules.isEmpty()) {
            return true
        }

        var matchedRule: RobotRule = matchedRules.first()
        for (rule in matchedRules) {
            if (rule.prefix.length > matchedRule.prefix.length) {
                matchedRule = rule
            } else if (rule.prefix.length == matchedRule.prefix.length && rule.isAllow && !matchedRule.isAllow) {
                matchedRule = rule
            }
        }
        return matchedRule.isAllow
    }

}