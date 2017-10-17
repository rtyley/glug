package glug.model.time

import java.lang.Math.min

import com.madgag.interval.Bound.{MAX, MIN}
import com.madgag.interval.IntervalClosure.CLOSED_OPEN
import com.madgag.interval.{AbstractInterval, Bound, IntervalClosure}
import org.joda.time.{Duration, Interval}

import scala.math.PartiallyOrdered

object LogInterval {

  def apply(duration: Duration, logInstantAtEnd: LogInstant): LogInterval =
    LogInterval(LogInstant(logInstantAtEnd.getMillis - duration.getMillis) , logInstantAtEnd)

  def apply(interval: Interval): LogInterval =
    LogInterval(LogInstant(interval.getStartMillis) , LogInstant(interval.getEndMillis))

  def intervalContainingDeltaFor(intervalA: LogInterval, intervalB: LogInterval): LogInterval = {
    if (intervalA == null || intervalB == null) return if (intervalA != null) intervalA
    else intervalB
    if (intervalA.start == intervalB.start) return new LogInterval(intervalA.end, intervalB.end)
    if (intervalA.end == intervalB.end) return new LogInterval(intervalA.start, intervalB.start)
    intervalA.union(intervalB)
  }

  def jodaIntervalFrom(interval: com.madgag.interval.Interval[LogInstant]) : org.joda.time.Interval=
    new Interval(interval.get(MIN).recordedInstantMillis, interval.get(MAX).recordedInstantMillis)
}

case class LogInterval(start: LogInstant, end: LogInstant)
  extends AbstractInterval[LogInstant] {

  def getStart: LogInstant = start

  def getEnd: LogInstant = end

  /**
    * Is this time interval entirely after the specified interval.
    * <p>
    * Intervals are inclusive of the start instant and exclusive of the end.
    */
  def isAfter(otherLogInterval: LogInterval): Boolean = !otherLogInterval.end.isAfter(start)

  def isAfter(otherInstant: LogInstant): Boolean = start.isAfter(otherInstant)

  /**
    * Is this time interval before the specified millisecond instant.
    * <p>
    * Intervals are inclusive of the start instant and exclusive of the end.
    */
  def isBefore(otherInstant: LogInstant): Boolean = !end.isAfter(otherInstant)

  def isBefore(otherInterval: LogInterval): Boolean = isBefore(otherInterval.getStart)

  def union(otherInterval: LogInterval): LogInterval = {
    if (otherInterval == null) return this
    if (contains(otherInterval)) return this
    if (otherInterval.contains(this)) return otherInterval
    val unionStart = if (start.isBefore(otherInterval.start)) start
    else otherInterval.start
    val unionEnd = if (end.isAfter(otherInterval.end)) end
    else otherInterval.end
    LogInterval(unionStart, unionEnd)
  }

  def toDurationMillis: Long = end.getMillis - start.getMillis

  override def toString: String = {
    val diffString = diff(start.getRecordedInstant.toString, end.getRecordedInstant.toString)
    diffString + ":lines=" + start.getLogLine + "-" + end.getLogLine
  }

  private def diff(s1: String, s2: String) = {
    val maxIndex = min(s1.length, s2.length)
    var index = 0
    while ( {
      index < maxIndex && s1.charAt(index) == s2.charAt(index)
    }) {
      index += 1; index
    }
    s1.substring(0, index) + "[" + s1.substring(index) + "|" + s2.substring(index) + "]"
  }

  def toJodaInterval = new Interval(start.getMillis, end.getMillis)

  def overlap(otherLogInterval: LogInterval): LogInterval = {
    if (!overlaps(otherLogInterval)) return null
    if (contains(otherLogInterval)) return otherLogInterval
    if (otherLogInterval.contains(this)) return this
    val overlapStart = if (start.isAfter(otherLogInterval.start)) start else otherLogInterval.start
    val overlapEnd = if (end.isBefore(otherLogInterval.end)) end else otherLogInterval.end

    LogInterval(overlapStart, overlapEnd)
  }

  override def get(bound: Bound): LogInstant = if (bound eq MIN) start else end

  override def getClosure: IntervalClosure = CLOSED_OPEN

}