package glug

import java.time.{Duration, Instant}
import java.util.concurrent.TimeUnit

import glug.model.time.{LogInstant, LogInterval}
import glug.model.{SignificantInterval, ThreadedSystem}
import fastparse.all._

import scala.collection.JavaConverters._
import scala.concurrent.duration.TimeUnit


//2017-10-04 09:08:00,037 [application-akka.actor.default-dispatcher-745] INFO  lib.DashboardData$ - completed lib.DashboardData$::topPagesQueryNonContentAgg in 121 ms elapsed, 116 ms elasticsearch
//2017-10-04 09:08:00,113 [application-akka.actor.default-dispatcher-745] INFO  lib.OnwardTraffic$ - completed lib.OnwardTraffic$::simpleDataForPaths in 75 ms elapsed, 74 ms elasticsearch
//2017-10-04 09:08:00,119 [application-akka.actor.default-dispatcher-745] INFO  lib.OnwardTraffic$ - completed lib.OnwardTraffic$::simpleDataForPaths in 82 ms elapsed, 80 ms elasticsearch
//2017-10-04 09:08:00,121 [ForkJoinPool-2-worker-3] INFO  AccessLog$ - 200 GET /top20chart?section=business in 236.6 ms
//2017-10-04 09:08:00,179 [application-akka.actor.default-dispatcher-746] INFO  c.HealthCheck$ - Lagcheck ok
//2017-10-04 09:08:00,179 [ForkJoinPool-2-worker-3] INFO  AccessLog$ - 200 GET /lag-check in 375.0 μs

case class LogLineStart(instant: Instant, threadName: String, logLevel: String, logger: String)

trait HasElapsedDuration {
  val elapsedDuration: Duration
}

case class ESCompleted(query: String, elapsedDuration: Duration, elasticSearchDuration: Duration) extends HasElapsedDuration
case class AccessCompleted(httpResponseCode: Int, path: String, elapsedDuration: Duration) extends HasElapsedDuration

class FooLogLineParser(threadedSystem: ThreadedSystem) {

  val digits: P[Long] = P(CharIn('0' to '9').rep(min = 1) !).map(_.toLong)
  val intDigits: P[Int] = P(CharIn('0' to '9').rep(min = 1) !).map(_.toInt)
  val decimal: P[Double] = P((CharIn('0' to '9') | ".").rep(min = 1)!).map(_.toDouble)

  val noSpace = CharPred(_ != ' ')
  val space = P(CharsWhileIn(" \r\n").rep(min = 1))

  val instant: P[Instant] =
    P((noSpace.rep(max = 12) ~ " " ~ noSpace.rep(max = 14)) !).map(t => Instant.parse(t.replace(' ', 'T').replace(',', '.') + "Z"))


  val identitifier = P((CharPred(c => c.isUnicodeIdentifierPart) | CharsWhileIn("-.$:")).rep(min = 1) !)

  val logLevel = P(StringIn("INFO", "WARN")) !

  val logLineStart: P[LogLineStart] = (instant ~ " [" ~ identitifier ~ "] " ~ logLevel ~ space ~ identitifier ~ " - ").map(LogLineStart.tupled)

  val duration: P[Duration] = P(digits ~ " ms").map(Duration.ofMillis)

  object StopwatchTimeParsing {
    val NANOSECONDS = P("ns").map(_ => TimeUnit.NANOSECONDS)

    val MICROSECONDS = P("\u03bcs").map(_ => TimeUnit.MICROSECONDS) // μs

    val MILLISECONDS = P("ms").map(_ => TimeUnit.MILLISECONDS)

    val SECONDS = P("s").map(_ => TimeUnit.SECONDS)

    val MINUTES = P("min").map(_ => TimeUnit.MINUTES)

    val HOURS = P("h").map(_ => TimeUnit.HOURS)

    val timeUnit: P[TimeUnit] = P(NANOSECONDS | MICROSECONDS | MILLISECONDS | SECONDS | MINUTES | HOURS)

    val stopwatchDuration: P[Duration] = P(decimal ~ " " ~ timeUnit).map { case (num, tu) => Duration.ofNanos(Math.round(num * tu.toNanos(1))) }
  }


  val elasticSearchCompletedLine: P[ESCompleted] = P("completed " ~ identitifier ~ " in " ~ duration ~ " elapsed, " ~ duration ~ " elasticsearch").map(ESCompleted.tupled)

  //2017-10-04 09:08:00,121 [ForkJoinPool-2-worker-3] INFO  AccessLog$ - 200 GET /top20chart?section=business in 236.6 ms
  val accessCompletedLine: P[AccessCompleted] = P(intDigits ~ " GET " ~ (noSpace.rep(min=1)!) ~ " in " ~ StopwatchTimeParsing.stopwatchDuration ).map(AccessCompleted.tupled)

  val recognisedLines = P((logLineStart ~ elasticSearchCompletedLine) | (logLineStart.filter(_.logger=="AccessLog$") ~ accessCompletedLine))

  def parse(line: String, lineNumber: Int): SignificantInterval = {
    println(s"looking at : '$line'")

    recognisedLines.parse(line).fold[SignificantInterval]((_,_,_) => null, {
      case ((lls, payload), i) =>
        println(s"got $lls $payload")


        val logInstantAtEnd = new LogInstant(lls.instant.toEpochMilli, lineNumber)
        val threadModel = threadedSystem.getOrCreateThread(lls.threadName)

        val logInterval = new LogInterval(org.joda.time.Duration.millis(payload.elapsedDuration.toMillis), logInstantAtEnd)

        val siPayload: Map[String, String] = payload match {
          case es: ESCompleted => Map("type" -> "ES Request", "Query" -> es.query)
          case ac: AccessCompleted =>Map("type" -> "Page Request", "Path" -> ac.path)
        }

        val significantInterval = new SignificantInterval(siPayload.asJava, logInterval)
        threadModel.add(significantInterval)
        significantInterval
    }
    )


  }
}
