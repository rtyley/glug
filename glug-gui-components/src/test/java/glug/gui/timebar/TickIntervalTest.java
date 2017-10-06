package glug.gui.timebar;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.joda.time.DateTimeFieldType.dayOfMonth;
import static org.joda.time.DateTimeFieldType.hourOfDay;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Test;


public class TickIntervalTest {

	@Test
	public void shouldClearTheMinorFieldsOfDateTime() {
		assertThat(new TickInterval(4,hourOfDay()).floor(new DateTime(2009,11,29,19,35,55,789)), equalTo(new DateTime(2009,11,29,16,0,0,0)));
	}
	
	@Test
	public void shouldClearTheHoursMinuteSecondsMillisOfDateTime() {
		assertThat(new TickInterval(1,dayOfMonth()).floor(new DateTime(2009,11,29,19,35,55,789)), equalTo(new DateTime(2009,11,29,0,0,0,0)));
	}
	
	@Test
	public void shouldIterateOverTheTickPointsForASpecifiedInterval() {
		Iterator<DateTime> iterator=new TickInterval(1,dayOfMonth()).ticksFor(new Interval(new DateTime(2009,11,25,19,10,3,789),new DateTime(2009,11,27,19,35,55,789)));
		assertThat(list(iterator),equalTo(asList(
				new DateTime(2009,11,25,0,0,0,0),
				new DateTime(2009,11,26,0,0,0,0),
				new DateTime(2009,11,27,0,0,0,0),
				new DateTime(2009,11,28,0,0,0,0))));
	}

	private <T> List<T> list(Iterator<T> iterator) {
		List<T> list = new ArrayList<T>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}
}
