package glug.gui.timelinecursor

import javax.swing.event.{ChangeEvent, ChangeListener}

import glug.model.time.{LogInstant, LogInterval}
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.{equalTo, nullValue}
import org.joda.time.Duration.standardSeconds
import org.junit.Test

object TimelineCursorTest {

  private[timelinecursor] class ChangeListenerStub extends ChangeListener {
    private var changeEvent: ChangeEvent = null

    override def stateChanged(changeEvent: ChangeEvent) {
      this.changeEvent = changeEvent
    }

    def getChangeEvent: ChangeEvent = changeEvent
  }

}

class TimelineCursorTest {
  @Test def shouldFireAChangeEventThatContainsCorrectOldAndNewState(): Unit = {
    val cursor = new TimelineCursor
    val intervalSelected = LogInterval(standardSeconds(5), LogInstant(8000, 0))
    cursor.setDot(intervalSelected.getStart)
    cursor.moveDot(intervalSelected.getEnd)
    val changeListenerStub = new TimelineCursorTest.ChangeListenerStub
    cursor.addChangeListener(changeListenerStub)
    val newDot = new LogInstant(18000, 0)
    cursor.setDot(newDot)
    val o = changeListenerStub.getChangeEvent.getSource.asInstanceOf[TimelineCursor.CursorPositionChanged]
    assertThat(o.getOldState.getSelectedInterval, equalTo(intervalSelected))
    assertThat(o.getNewState.getSelectedInterval, nullValue)
    assertThat(o.getNewState.getDot, equalTo(newDot))
  }
}