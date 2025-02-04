package com.jonquass.whiteglove.data.web

import crawlercommons.robots.SimpleRobotRules
import crawlercommons.robots.SimpleRobotRules.RobotRule
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.net.URI
import kotlin.test.assertTrue

class RobotsTxtManagerTest {

    private var robotsTxtManager: RobotsTxtManager = RobotsTxtManager()

    /**
     * @see <a href="https://developers.google.com/search/docs/crawling-indexing/robots/robots_txt#url-matching-based-on-path-values">URL Matching Based on Path Values</a>
     */

    @ParameterizedTest
    @CsvSource(
        textBlock = """
            /fish, true
            /fish.html, true
            /fish/salmon.html, true
            /fishheads, true
            /fishheads/yummy.html, true
            /fish.php?id=anything, true
            /Fish.asp, false
            /catfish, false
            /?id=fish, false
            /desert/fish, false"""
    )
    fun itShouldMatchAnyPathThatStartsWithFish(path: String, shouldMatch: Boolean) {
        val robotRule = RobotRule("/fish", true)
        val uri = URI("https://www.website.com$path")

        val ruleMatches = robotsTxtManager.ruleMatchesUrl(robotRule, uri)

        assertTrue { shouldMatch == ruleMatches }
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
            /fish, true
            /fish.html, true
            /fish/salmon.html, true
            /fishheads, true
            /fishheads/yummy.html, true
            /fish.php?id=anything, true
            /Fish.asp, false
            /catfish, false
            /?id=fish, false
            /desert/fish, false"""
    )
    fun itShouldIgnoreTrailingWildcard(path: String, shouldMatch: Boolean) {
        val robotRule = RobotRule("/fish*", true)
        val uri = URI("https://www.website.com$path")

        val ruleMatches = robotsTxtManager.ruleMatchesUrl(robotRule, uri)

        assertTrue { shouldMatch == ruleMatches }


    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
            /fish/, true
            /fish/?id=anything, true
            /fish/salmon.htm, true
            /fish, false
            /fish.html, false
            /animals/fish, false
            /Fish/Salmon.asp, false"""
    )
    fun itShouldMatchAnythingInTheFishFolder(path: String, shouldMatch: Boolean) {
        val robotRule = RobotRule("/fish/", true)
        val uri = URI("https://www.website.com$path")

        val ruleMatches = robotsTxtManager.ruleMatchesUrl(robotRule, uri)

        assertTrue { shouldMatch == ruleMatches }
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
            /index.php, true
            /filename.php, true
            /folder/filename.php, true
            /folder/filename.php?parameters, true
            /folder/any.php.file.html, true
            /filename.php/, true
            /, false
            /windows.PHP, false"""
    )
    fun itShouldMatchAnyPathThatContainsWildcardPhp(path: String, shouldMatch: Boolean) {
        val robotRule = RobotRule("/*.php", true)
        val uri = URI("https://www.website.com$path")

        val ruleMatches = robotsTxtManager.ruleMatchesUrl(robotRule, uri)

        assertTrue { shouldMatch == ruleMatches }
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
            /filename.php, true
            /folder/filename.php, true
            /filename.php?parameters, false
            /filename.php/, false
            /filename.php5, false
            /windows.PHP, false"""
    )
    fun itShouldMatchAnyPathThatEndsWithPhp(path: String, shouldMatch: Boolean) {
        val robotRule = RobotRule("/*.php$", true)
        val uri = URI("https://www.website.com$path")

        val ruleMatches = robotsTxtManager.ruleMatchesUrl(robotRule, uri)

        assertTrue { shouldMatch == ruleMatches }
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
            /fish.php, true
            /fishheads/catfish.php?parameters, true
            /Fish.PHP, false"""
    )
    fun itShouldMatchAnyPathThatContainsFishAndPhpInThatOrder(path: String, shouldMatch: Boolean) {
        val robotRule = RobotRule("/fish*.php", true)
        val uri = URI("https://www.website.com$path")

        val ruleMatches = robotsTxtManager.ruleMatchesUrl(robotRule, uri)

        assertTrue { shouldMatch == ruleMatches }
    }

    //

    /**
     * @see <a href="https://developers.google.com/search/docs/crawling-indexing/robots/robots_txt#order-of-precedence-for-rules">Order of Precedence for Rules</a>
     */

    @ParameterizedTest
    @CsvSource(
        textBlock = """
            https://example.com/page, /p, /, true
            https://example.com/folder/page, /folder, /folder, true
            https://example.com/page.htm, /page, /*.ph, true
            https://example.com/page.php5, /page, /*.ph, true
            https://example.com/, /$, /, true
            https://example.com/page.htm, /$, /, false"""
    )
    fun itShouldHandleOrderOfPrecedence(
        urlString: String,
        allowRule: String,
        disallowRule: String,
        shouldAllow: Boolean
    ) {
        val link = URI(urlString)
        val robotRules = SimpleRobotRules()
        robotRules.addRule(allowRule, true)
        robotRules.addRule(disallowRule, false)

        val shouldScrape = robotsTxtManager.shouldScrape(link, robotRules)

        assertTrue { shouldAllow == shouldScrape }
    }

}
