package glug.model.time

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.is
import org.scalatest.{FlatSpec, Matchers}

class LogIntervalTest extends FlatSpec with Matchers {
  "Zero duration LogIntervals" should "be correctly ordered even if they differ only by line number" in {
    val recordedInstant = 1234L
    val logInstantA = LogInstant(recordedInstant, 344)
    val logInstantB = LogInstant(recordedInstant, 345)
    val logIntervalAtA = LogInterval(logInstantA, logInstantA)
    val logIntervalAtB = LogInterval(logInstantB, logInstantB)

    logIntervalAtA.isBefore(logIntervalAtB) shouldBe true
    logIntervalAtB.isAfter(logIntervalAtA) shouldBe true
  }

  "LogInterval" should "implement contains()" in {
    val bigLogInterval = LogInterval(LogInstant(1000L, 1), LogInstant(5000L, 5))
    val smallLogInterval = LogInterval(LogInstant(3000L, 3), LogInstant(4000L, 4))

    bigLogInterval.contains(smallLogInterval) shouldBe true
    smallLogInterval.contains(bigLogInterval) shouldBe false
  }

  it should "determine ordering of adjacent intervals" in {
    val earlierLogInterval = LogInterval(LogInstant(1000L, 1), LogInstant(3000L, 3))
    val laterLogInterval = LogInterval(LogInstant(3000L, 3), LogInstant(5000L, 5))

    earlierLogInterval.isBefore(laterLogInterval) shouldBe true
    earlierLogInterval.isAfter(laterLogInterval) shouldBe false

    laterLogInterval.isAfter(earlierLogInterval) shouldBe true
    laterLogInterval.isBefore(earlierLogInterval) shouldBe false
  }

  it should "determine ordering of overlapping intervals" in {
    val earlierOverlapping = LogInterval(LogInstant(1000L, 1), LogInstant(3000L, 3))
    val laterOverlapping = LogInterval(LogInstant(2000L, 2), LogInstant(4000L, 4))

    earlierOverlapping.isAfter(laterOverlapping) shouldBe false
    earlierOverlapping.isBefore(laterOverlapping) shouldBe false

    laterOverlapping.isBefore(earlierOverlapping) shouldBe false
    laterOverlapping.isAfter(earlierOverlapping) shouldBe false
  }

  it should "determine if it is before or after a given instant" in {
    val interval = LogInterval(LogInstant(1000L, 2), LogInstant(2000L, 4))

    val instantBeforeInterval = LogInstant(500L, 1)
    interval.isAfter(instantBeforeInterval) shouldBe true
    interval.isBefore(instantBeforeInterval) shouldBe false

    val instantDuringInterval = LogInstant(1500L, 3)
    interval.isAfter(instantDuringInterval) shouldBe false
    interval.isBefore(instantDuringInterval) shouldBe false

    val instantAfterInterval = LogInstant(3000L, 5)
    interval.isAfter(instantAfterInterval) shouldBe false
    interval.isBefore(instantAfterInterval) shouldBe true
  }

  it should "be a half-open interval with respect to Instants" in {
    val lowerBound = LogInstant(1000L, 1)
    val upperBound = LogInstant(2000L, 2)
    val interval = LogInterval(lowerBound, upperBound)
    interval.isAfter(lowerBound) shouldBe false
    interval.isBefore(upperBound) shouldBe true
  }

  it should "count milliseconds without endpoint" in {
    val lowerBound = LogInstant(1L, 1)
    val upperBound = LogInstant(2L, 2)
    // Only the millisecond at 1 is in the interval.
    assertThat(LogInterval(lowerBound, upperBound).toDurationMillis, is(1L))
  }
}