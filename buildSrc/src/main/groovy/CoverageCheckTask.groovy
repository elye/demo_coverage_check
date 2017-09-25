package custom.build.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

class CoverageCheckTask extends DefaultTask {
    String reportPath
    String coverageRequired

    @TaskAction
    def testCoverageVerification() {
        reportPath = project.coverageCheckConfig.reportPath
        coverageRequired = project.coverageCheckConfig.coverageRequired
        def parser = getXMLParserInstance()
        def expectedCoverageMetricsFile = new File((String) coverageRequired)
        def actualCoverageMetrics = calculateCoverageMetrics(parser, "${reportPath}")
        def failures = compare(expectedCoverageMetricsFile, actualCoverageMetrics)
        printVerificationResult(failures)
    }

    def compare(File expectedCoverageMetricsFile, LinkedHashMap actualCoverageMetrics) {
        def failures = []
        def expectedCoverageMetrics = new Properties()
        def newGeneratedCoverageMetrics = new StringBuilder()

        expectedCoverageMetricsFile.withInputStream { expectedCoverageMetrics.load(it) }
        actualCoverageMetrics.each {
            def actualCoverageMetric = it.value
            def expectedCoverageMetric = Double.valueOf(expectedCoverageMetrics[it.key])
            def newGeneratedCoverageMetric = expectedCoverageMetric

            if (actualCoverageMetric < expectedCoverageMetric) {
                failures.add("- ${it.key} coverage is: ${actualCoverageMetric}%, expecting ${expectedCoverageMetric}%")
            } else {
                newGeneratedCoverageMetric = actualCoverageMetric
            }

            newGeneratedCoverageMetrics.append("${it.key}=${(int) Math.floor(newGeneratedCoverageMetric)}\n")
        }

        expectedCoverageMetricsFile.newWriter().withWriter { w -> w << newGeneratedCoverageMetrics }
        return failures
    }

    def printVerificationResult(ArrayList failures) {
        if (failures) {
            logger.quiet("------------------ Code Coverage Failed -----------------------")
            failures.each {
                logger.quiet(it)
            }
            logger.quiet("---------------------------------------------------------------")
            throw new GradleException("Code coverage failed")
        } else {
            logger.quiet("Passed Code Coverage Checks")
        }
    }

    def calculateCoverageMetrics(XmlParser parser, String reportPath) {
        def counters = parser.parse(new File(reportPath)).counter
        def percentage = {
            try {
                def covered = it.'@covered' as Double
                def missed = it.'@missed' as Double
                ((covered / (covered + missed)) * 100).round(2)
            } catch (Exception e) {
                logger.quiet("Error in % calculation. Report as 0%")
                return 0
            }
        }

        def metrics = [:]
        metrics << [
                'instruction': percentage(counters.find { (it.'@type' == 'INSTRUCTION') }),
                'branch'     : percentage(counters.find { (it.'@type' == 'BRANCH') }),
                'line'       : percentage(counters.find { (it.'@type' == 'LINE') }),
                'complexity' : percentage(counters.find { (it.'@type' == 'COMPLEXITY') }),
                'method'     : percentage(counters.find { (it.'@type' == 'METHOD') }),
                'class'      : percentage(counters.find { (it.'@type' == 'CLASS') })
        ]
        return metrics
    }

    def getXMLParserInstance() {
        def parser = new XmlParser()
        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
        return parser
    }
}
