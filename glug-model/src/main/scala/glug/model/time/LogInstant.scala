package glug.model.time

import org.joda.time.Instant

object LogInstant {
  def apply(recordedInstant: Instant): LogInstant =
    LogInstant(recordedInstant.getMillis, 0)
}

case class LogInstant(
  recordedInstantMillis: Long,
  logLineNumber: Int = 0
) extends Ordered[LogInstant] {

  def getMillis: Long = recordedInstantMillis

  def isAfter(otherLogInstant: LogInstant): Boolean =
    (recordedInstantMillis > otherLogInstant.recordedInstantMillis) || (recordedInstantMillis == otherLogInstant.recordedInstantMillis && logLineNumber > otherLogInstant.logLineNumber)

  def isBefore(otherLogInstant: LogInstant): Boolean =
    (recordedInstantMillis < otherLogInstant.recordedInstantMillis) || (recordedInstantMillis == otherLogInstant.recordedInstantMillis && logLineNumber < otherLogInstant.logLineNumber)

  def getRecordedInstant = new Instant(getMillis)

  def getLogLine: Int = logLineNumber

  override def toString: String = getRecordedInstant + ":line=" + logLineNumber

  override def compare(otherLogInstant: LogInstant) =
    if (isAfter(otherLogInstant)) 1 else if (isBefore(otherLogInstant)) -1 else 0
}