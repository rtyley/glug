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
				tick(1,dayOfMonth()),
				tick(4,hourOfDay()), tick(1,hourOfDay()),
				tick(10,minuteOfHour()),   tick(5,minuteOfHour()),   tick(1,minuteOfHour()),
				tick(10,secondOfMinute()), tick(5,secondOfMinute()), tick(1,secondOfMinute()),
				tick(100,millisOfSecond()),tick(10,millisOfSecond()),tick(1,millisOfSecond()));
		
		assertThat(ps.rangeFor(standardSeconds(4), standardMinutes(8)).lastEntry().getValue(),equalTo(tick(10,minuteOfHour())));
		assertThat(ps.rangeFor(standardSeconds(4), standardMinutes(8)).firstEntry().getValue(),equalTo(tick(5,secondOfMinute())));
		assertThat(ps.rangeFor(standardSeconds(5), standardMinutes(8)).firstEntry().getValue(),equalTo(tick(5,secondOfMinute())));
		assertThat(ps.rangeFor(standardSeconds(5), standardDays(3)).lastEntry().getValue(),equalTo(tick(1,dayOfMonth())));
	}
}
