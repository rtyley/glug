package com.gu.glug.gui;

import static com.gu.glug.gui.TickInterval.tick;
import static org.hamcrest.Matchers.equalTo;
import static org.joda.time.DateTimeFieldType.dayOfMonth;
import static org.joda.time.DateTimeFieldType.hourOfDay;
import static org.joda.time.DateTimeFieldType.millisOfSecond;
import static org.joda.time.DateTimeFieldType.minuteOfHour;
import static org.joda.time.DateTimeFieldType.secondOfMinute;
import static org.joda.time.Duration.standardDays;
import static org.joda.time.Duration.standardMinutes;
import static org.joda.time.Duration.standardSeconds;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class TickIntervalSetTest {

	@Test
	public void shouldReturnWhatIWantDammit() {
		TickIntervalSet ps = new TickIntervalSet(
				tick(1,dayOfMonth(),null),
				tick(4,hourOfDay(),null), tick(1,hourOfDay(),null),
				tick(10,minuteOfHour(),null),   tick(5,minuteOfHour(),null),   tick(1,minuteOfHour(),null),
				tick(10,secondOfMinute(),null), tick(5,secondOfMinute(),null), tick(1,secondOfMinute(),null),
				tick(100,millisOfSecond(),null),tick(10,millisOfSecond(),null),tick(1,millisOfSecond(),null));
		
		assertThat(ps.rangeFor(standardSeconds(4), standardMinutes(8)).lastEntry().getValue(),equalTo(tick(10,minuteOfHour(),null)));
		assertThat(ps.rangeFor(standardSeconds(4), standardMinutes(8)).firstEntry().getValue(),equalTo(tick(5,secondOfMinute(),null)));
		assertThat(ps.rangeFor(standardSeconds(5), standardMinutes(8)).firstEntry().getValue(),equalTo(tick(5,secondOfMinute(),null)));
		assertThat(ps.rangeFor(standardSeconds(5), standardDays(3)).lastEntry().getValue(),equalTo(tick(1,dayOfMonth(),null)));
	}
}
