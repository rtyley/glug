package glug.gui.timelinecursor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.joda.time.Duration.standardSeconds;
import glug.gui.timelinecursor.TimelineCursor.CursorPositionChanged;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.junit.Test;


public class TimelineCursorTest {

	@Test
	public void shouldFireAChangeEventThatContainsCorrectOldAndNewState() {
		TimelineCursor cursor = new TimelineCursor();
		LogInterval intervalSelected = new LogInterval(standardSeconds(5),new LogInstant(8000));
		cursor.setDot(intervalSelected.getStart());
		cursor.moveDot(intervalSelected.getEnd());
		
		ChangeListenerStub changeListenerStub = new ChangeListenerStub();
		cursor.addChangeListener(changeListenerStub);
		
		LogInstant newDot = new LogInstant(18000);
		cursor.setDot(newDot);
		
		TimelineCursor.CursorPositionChanged o = (CursorPositionChanged) changeListenerStub.getChangeEvent().getSource();
		assertThat(o.getOldState().getSelectedInterval(), equalTo(intervalSelected));
		assertThat(o.getNewState().getSelectedInterval(), nullValue());
		assertThat(o.getNewState().getDot(), equalTo(newDot));
		
	}
	
	static class ChangeListenerStub implements ChangeListener {

		private ChangeEvent changeEvent;

		@Override
		public void stateChanged(ChangeEvent changeEvent) {
			this.changeEvent = changeEvent;
		}
		
		public ChangeEvent getChangeEvent() {
			return changeEvent;
		}
		
	}
}
