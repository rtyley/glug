package glug.gui

import java.lang.System.currentTimeMillis
import java.util
import javax.swing._

import com.madgag.interval.Interval
import com.madgag.interval.SimpleInterval.union
import glug.model.time.LogInterval.jodaIntervalFrom
import glug.model.time.{LogInstant, LogInterval}
import glug.parser.LogLoader
import glug.parser.LogLoader.LoadReport
import org.joda.time.format.PeriodFormat

import scala.collection.JavaConverters._

class LogLoadingTask(val logLoader: LogLoader, val uiUpdater: DataLoadedUIUpdater, val numLinesLoadedBetweenUIUpdates: Int) extends SwingWorker[Void, LogLoader.LoadReport] {
  override def doInBackground: Void = {
    val startLoadTime = currentTimeMillis
    var loadReport: LoadReport = null
    var loadedLogInterval: Interval[LogInstant] = null
    try do {
      loadReport = logLoader.loadLines(numLinesLoadedBetweenUIUpdates)
      publish(loadReport)
      loadedLogInterval = union(loadedLogInterval, loadReport.getUpdatedInterval)
      //System.out.print(".");
    } while ( {
      !isCancelled && !loadReport.endOfStreamReached
    })
    catch {
      case e: Throwable =>
        e.printStackTrace()
        throw new RuntimeException(e)
    }
    val durationLoadTime = currentTimeMillis - startLoadTime
    System.out.println("Finished loading " + loadedLogInterval + " (" + format(loadedLogInterval) + ") in " + durationLoadTime + " ms")
    null
  }

  private def format(loadedLogInterval: Interval[LogInstant]) =
    jodaIntervalFrom(loadedLogInterval).toPeriod.toString(PeriodFormat.getDefault)

  override protected def process(loadReports: util.List[LogLoader.LoadReport]): Unit = {
    val totalLogIntervalCoveredByLoadReports = totalLogIntervalCoveredBy(loadReports.asScala)
    System.out.println("Just loaded " + totalLogIntervalCoveredByLoadReports)
    uiUpdater.updateUI(totalLogIntervalCoveredByLoadReports)
  }

  private def totalLogIntervalCoveredBy(loadReports: Iterable[LogLoader.LoadReport]) = {
    loadReports.map(_.getUpdatedInterval).reduce(union(_,_))
  }
}