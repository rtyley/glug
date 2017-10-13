package glug

import glug.model.ThreadedSystem
import org.scalatest._

import scala.collection.mutable.Stack

class FooLogLineParserTest extends FlatSpec with Matchers {

  private val fooLogLineParser = new FooLogLineParser(new ThreadedSystem())

  "A line parser" should "understand a sample line" in {
    val sample = "2017-10-04 09:08:00,037 [application-akka.actor.default-dispatcher-745] INFO  lib.DashboardData$ - completed lib.DashboardData$::topPagesQueryNonContentAgg in 121 ms elapsed, 116 ms elasticsearch"
    val boo = fooLogLineParser.recognisedLines.parse(sample)
    println(boo.get.value)
  }

  it should "parse an log line start" in {
    val sample = "2017-10-04 09:08:00,121 [ForkJoinPool-2-worker-3] INFO  AccessLog$ - 200 GET /top20chart?section=business in 236.6 ms"
    val boo = fooLogLineParser.logLineStart.parse(sample)
    println(boo.get.value)
  }

  it should "parse an access log line" in {
    val sample = "2017-10-04 09:08:00,121 [ForkJoinPool-2-worker-3] INFO  AccessLog$ - 200 GET /top20chart?section=business in 236.6 ms"
    val boo = fooLogLineParser.recognisedLines.parse(sample)
    println(boo.get.value)
  }

  it should "parse the payload of an access log line" in {
    val sample = "200 GET /top20chart?section=business in 236.6 ms"
    val boo = fooLogLineParser.accessCompletedLine.parse(sample)
    println(boo.get.value)
  }
}