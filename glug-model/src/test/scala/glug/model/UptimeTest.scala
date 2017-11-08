package glug.model

import glug.model.time.{LogInstant, LogInterval}
import org.joda.time.Duration.standardSeconds
import org.joda.time.Instant
import org.scalatest.{FlatSpec, Matchers}

class UptimeTest extends FlatSpec with Matchers {

  "Uptime" should "return correct uptime for instant" in {
    val uptime = new Uptime
    uptime.addUptime(LogInterval(standardSeconds(6), LogInstant(10000)))
    uptime.at(new Instant(3000)) shouldBe null
    uptime.at(new Instant(4000)) shouldBe standardSeconds(0)
    uptime.at(new Instant(5000)) shouldBe standardSeconds(1)
    uptime.at(new Instant(10000)) shouldBe standardSeconds(6)
    uptime.at(new Instant(11000)) shouldBe standardSeconds(7)
  }
}