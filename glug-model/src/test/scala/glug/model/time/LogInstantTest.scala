package glug.model.time

import org.joda.time.Instant
import org.scalatest._

class LogInstantTest extends FlatSpec with Matchers {

  "LogInstant" should "be correctly ordered even if they differ only by line number" in {
    val recordedInstant = 1234L
    val earlyLogInstant = LogInstant(recordedInstant, 344)
    val laterLogInstant = LogInstant(recordedInstant, 345)

    earlyLogInstant should be < laterLogInstant
    laterLogInstant should be > earlyLogInstant
  }
}