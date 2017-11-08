package glug.model.time

import java.util.Collections

import com.google.common.collect.ImmutableMap.of
import glug.model.{SignificantInterval, ThreadModel}
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.joda.time.{Duration, Interval}
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.is
import org.scalatest.{Entry, FlatSpec, Matchers}

import scala.collection.JavaConverters._

class ThreadModelTest extends FlatSpec with Matchers {

  "ThreadModel" should "ReturnEmptySetIfNoBuggerMatches" in {
    val thread = new ThreadModel("blahthread", null)
    new SignificantInterval(Map("type" -> "My Type").asJava, LogInterval(Duration.standardSeconds(1), LogInstant(1000, 1)))
    val instantWhereNoDamnThingWasHappening = LogInstant.apply(5000, 5)

    thread.getSignificantIntervalsFor(instantWhereNoDamnThingWasHappening).asScala shouldBe empty
  }

  it should "BoggleToAswad" in {
    val thread = new ThreadModel("blahthread", null)
    thread.add(new SignificantInterval(Map("type"-> "My Type").asJava, LogInterval(new Interval(3000, 7000))))
    thread.countOccurencesDuring(LogInterval(new Interval(2000, 8000)), "My Type") should contain (Entry("My Type", 1))
  }
}