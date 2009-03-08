package com.gu.glug.gui;

import static org.hamcrest.Matchers.equalTo;
import static org.joda.time.Duration.standardDays;
import static org.joda.time.Duration.standardMinutes;
import static org.joda.time.Duration.standardSeconds;
import static org.joda.time.Period.days;
import static org.joda.time.Period.hours;
import static org.joda.time.Period.millis;
import static org.joda.time.Period.minutes;
import static org.joda.time.Period.seconds;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class PeriodSetTest {

	@Test
	public void shouldReturnWhatIWantDammit() {
		PeriodSet ps = new PeriodSet(
				days(1),
				hours(4),hours(1),
				minutes(10),minutes(5),minutes(1),
				seconds(10),seconds(5),seconds(1),
				millis(100),millis(10),millis(1));
		
		assertThat(ps.rangeFor(standardSeconds(4), standardMinutes(8)).lastEntry().getValue(),equalTo(minutes(10)));
		assertThat(ps.rangeFor(standardSeconds(4), standardMinutes(8)).firstEntry().getValue(),equalTo(seconds(5)));
		assertThat(ps.rangeFor(standardSeconds(5), standardMinutes(8)).firstEntry().getValue(),equalTo(seconds(5)));
		assertThat(ps.rangeFor(standardSeconds(5), standardDays(3)).lastEntry().getValue(),equalTo(days(1)));
	}
}
