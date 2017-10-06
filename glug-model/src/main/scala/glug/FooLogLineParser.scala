package glug

import java.time.{Duration, Instant}

import glug.model.time.{LogInstant, LogInterval}
import glug.model.{SignificantInterval, ThreadedSystem}

import fastparse.all._

import scala.collection.JavaConverters._


//2017-10-04 09:08:00,037 [application-akka.actor.default-dispatcher-745] INFO  lib.DashboardData$ - completed lib.DashboardData$::topPagesQueryNonContentAgg in 121 ms elapsed, 116 ms elasticsearch
//2017-10-04 09:08:00,113 [application-akka.actor.default-dispatcher-745] INFO  lib.OnwardTraffic$ - completed lib.OnwardTraffic$::simpleDataForPaths in 75 ms elapsed, 74 ms elasticsearch
//2017-10-04 09:08:00,119 [application-akka.actor.default-dispatcher-745] INFO  lib.OnwardTraffic$ - completed lib.OnwardTraffic$::simpleDataForPaths in 82 ms elapsed, 80 ms elasticsearch
//2017-10-04 09:08:00,121 [ForkJoinPool-2-worker-3] INFO  AccessLog$ - 200 GET /top20chart?section=business in 236.6 ms
//2017-10-04 09:08:00,179 [application-akka.actor.default-dispatcher-746] INFO  c.HealthCheck$ - Lagcheck ok
//2017-10-04 09:08:00,179 [ForkJoinPool-2-worker-3] INFO  AccessLog$ - 200 GET /lag-check in 375.0 μs

case class LogLineStart(instant: Instant, threadName: String, logLevel: String, logger: String)


class FooLogLineParser(threadedSystem: ThreadedSystem) {

  val digits: P[Long] = P(CharIn('0' to '9').rep(min = 1) !).map(_.toLong)

  val noSpace = CharPred(_ != ' ')
  val space = P(CharsWhileIn(" \r\n").rep(min = 1))

  val instant: P[Instant] =
    P((noSpace.rep(max = 12) ~ " " ~ noSpace.rep(max = 14)) !).map(t => Instant.parse(t.replace(' ', 'T').replace(',', '.') + "Z"))


  val identitifier = P((CharPred(c => c.isUnicodeIdentifierPart) | CharsWhileIn("-.$:")).rep(min = 1) !)

  val logLevel = P(StringIn("INFO", "WARN")) !

  val logLineStart: P[LogLineStart] = (instant ~ " [" ~ identitifier ~ "] " ~ logLevel ~ space ~ identitifier ~ " - ").map(LogLineStart.tupled)

  val duration: P[Duration] = P(digits ~ " ms").map(Duration.ofMillis)

  val elasticSearchCompletedLine = P(logLineStart ~ "completed " ~ identitifier ~ " in " ~ duration ~ " elapsed, " ~ duration ~ " elasticsearch")

  def parse(line: String, lineNumber: Int): SignificantInterval = {
    println(s"looking at : '$line'")

    elasticSearchCompletedLine.parse(line).fold[SignificantInterval]((_,_,_) => null, {
      case ((lls, query, elapsedDuration, elasticSearchDuration), i) =>
        println(s"got $lls")

        val logInstantAtEnd = new LogInstant(lls.instant.toEpochMilli, lineNumber)
        val threadModel = threadedSystem.getOrCreateThread(lls.threadName)

        val logInterval = new LogInterval(org.joda.time.Duration.millis(elapsedDuration.toMillis), logInstantAtEnd)
        val significantInterval = new SignificantInterval(Map("type" -> "ES Request", "Query" -> query).asJava,
          logInterval)
        threadModel.add(significantInterval)
        significantInterval
    }
    )


  }
}
