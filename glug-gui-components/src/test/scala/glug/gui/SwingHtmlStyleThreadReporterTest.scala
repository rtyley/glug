package glug.gui

import java.awt.Color.RED

import glug.model.ThreadedSystem
import glug.model.time.{LogInstant, LogInterval}
import org.joda.time.Duration.standardSeconds
import org.scalatest.{FlatSpec, Matchers}
import org.scalactic.StringNormalizations._

class SwingHtmlStyleThreadReporterTest extends FlatSpec with Matchers {

  val reporter = new SwingHtmlStyleThreadReporter

  "reporter" should "cope with absent IntervalType for requested instant" in {
    val threadedSystem = new ThreadedSystem
    val thread = threadedSystem.getOrCreateThread("my-thread")
    reporter.htmlSyledReportFor(thread, new LogInstant(1000, 0))
  }

  it should "include uptime if known" in {
    val threadedSystem = new ThreadedSystem
    threadedSystem.uptime.addUptime(LogInterval.apply(standardSeconds(3), new LogInstant(3000, 0)))
    reporter.uptimeStringFor(threadedSystem, LogInstant.apply(1000)) should include("uptime: 1.000 s")
  }

  it should "NotThrowExceptionIfUptimeIsNotKnown" in {
    val threadedSystem = new ThreadedSystem
    reporter.uptimeStringFor(threadedSystem, LogInstant.apply(1000)) shouldBe ""
  }

  it should "shouldReturnRightHexForColour" in {
    reporter.hexFor(RED) should equal("ff0000") (after being lowerCased)
  }
}