package com.gu.glug.gui;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

public class TimelineDateTimeComponentTest {
	private TimelineDateTimeComponent timelineDateTimeComponent;
	private UITimeScale timeScale;

	@Before
	public void setUp() {
		timeScale = new UITimeScale();
		timelineDateTimeComponent = new TimelineDateTimeComponent(timeScale);
		
	}
	
	@Test
	public void shouldGiveZeroHeightTicksIfTicksAreSpacedCloseTogether() {
		for (int tickSpacingInPixels = 0; tickSpacingInPixels < 8; ++tickSpacingInPixels) {
			assertThat(timelineDateTimeComponent.getTickHeightForTicksSpaced(tickSpacingInPixels),	equalTo(0));
		}
	}

	@Test
	public void shouldGiveMaxHeightTicksIfTicksAreSpacedFarApart() {
		for (int tickSpacingInPixels = 100; tickSpacingInPixels < 1000; ++tickSpacingInPixels) {
			assertThat(timelineDateTimeComponent.getTickHeightForTicksSpaced(tickSpacingInPixels),	equalTo(12));
		}
	}
	
	@Test
	public void shouldUseTenSecondsAsMinorTicksIfTenSecondsOfPixelsIsLessThan16PixelsApart() {
		timeScale.setMillisecondsPerPixel(Period.seconds(10).minusMillis(1).toStandardDuration().getMillis() / 16d);
		Period period = timelineDateTimeComponent.getPeriodOfIntevalBetweenMinorTicks();
		assertThat(period,equalTo(Period.seconds(10)));
	}
	
	@Test
	public void shouldUseSecondsAsMinorTicksIfOneSecondOfPixelsIsLessThan16PixelsApart() {
		timeScale.setMillisecondsPerPixel(Period.seconds(1).minusMillis(1).toStandardDuration().getMillis() / 16d);
		Period period = timelineDateTimeComponent.getPeriodOfIntevalBetweenMinorTicks();
		assertThat(period,equalTo(Period.seconds(1)));
	}
	
	@Test
	public void shouldUseMinutesAsMinorTicksIfOneMinuteOfPixelsIsLessThan16PixelsApart() {
		timeScale.setMillisecondsPerPixel(Period.minutes(1).minusSeconds(1).toStandardDuration().getMillis() / 16d);
		Period period = timelineDateTimeComponent.getPeriodOfIntevalBetweenMinorTicks();
		assertThat(period,equalTo(Period.minutes(1)));
	}
}
