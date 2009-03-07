package com.gu.glug.gui;

import static java.lang.Math.round;
import static java.util.Arrays.asList;
import static org.joda.time.Period.days;
import static org.joda.time.Period.hours;
import static org.joda.time.Period.millis;
import static org.joda.time.Period.minutes;
import static org.joda.time.Period.seconds;
import static org.joda.time.Period.weeks;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import javax.swing.JComponent;

import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public class TimelineDateTimeComponent extends JComponent {
	
	NavigableSet<Period> periods = getPeriods();
	
	private static NavigableSet<Period> getPeriods() {
		List<Period> periodList = asList(
				days(1),
				hours(4),hours(1),
				minutes(10),minutes(5),minutes(1),
				seconds(10),seconds(5),seconds(1),
				millis(100),millis(10),millis(1)
		);
		TreeSet<Period> periodSet = new TreeSet<Period>();
		return periodSet;
	}

	/*
2008	2009	2010	-- 1 year

2009-02	2009-03	2009-04	-- 1 month

2009-02-25	2009-02-26	2009-02-27	-- 1 day

2009-02-25 08:00	2009-02-25 12:00	2009-02-25 16:00	2009-02-25 20:00	-- 4 hours

16:00	17:00	18:00	19:00	-- 1 hour

16:00	16:10	16:20	16:30	-- 10 minutes

16:00	16:05	16:10	16:15	-- 5 minutes

16:00	16:01	16:02	16:03	-- 1 minute

16:44:00	16:44:10	16:45:20	-- 10 seconds

16:44:35	16:44:40	16:44:45	-- 5 seconds

16:44:37	16:44:38	16:44:39	-- 1 second

16:12:01.200	16:12:01.300	16:12:01.400	-- 100 ms

16:12:01.200	16:12:01.210	16:12:01.220	-- 10 ms

16:12:01.200	16:12:01.201	16:12:01.202	-- 1 ms
*/	
	private static final long serialVersionUID = 1L;
	
	private static final PeriodType periodUnits = PeriodType.standard(); // Ticks are placed at the units used by this period
	int approxPixelsDesiredBetweenMinorTicks = 16;

	private final UITimeScale timeScale;

	public TimelineDateTimeComponent(UITimeScale timeScale) {
		this.timeScale = timeScale;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2D = (Graphics2D) g;
		Rectangle clipBounds = graphics2D.getClipBounds();
		
		double approxGoodTimeSeparationForMinorTicksInMillis = timeScale.getMillisecondsPerPixel();
		
		int pixelsBetweenTicks = 16;
		int tickHeight = getTickHeightForTicksSpaced(pixelsBetweenTicks);
	}

	int getTickHeightForTicksSpaced(int pixelsBetweenTicks) {
		// TODO Auto-generated method stub
		//periods.floor(e)
		return 0;
	}

	public Period getPeriodOfIntevalBetweenMinorTicks() {
		DurationFieldType smallestEmptyField = getDurationFieldTypeOfIntervalBetweenMinorTicks();
		Period periodForMinorTicks = new Period(Duration.ZERO,periodUnits).withField(smallestEmptyField, 1);
		return periodForMinorTicks;
	}

	private DurationFieldType getDurationFieldTypeOfIntervalBetweenMinorTicks() {
		Period period = new Period(round(timeScale.getMillisecondsPerPixel()*approxPixelsDesiredBetweenMinorTicks),periodUnits);
		period.toStandardSeconds();
		
		DurationFieldType smallestEmptyField = null;
		for (int fieldIndex = 0; fieldIndex<periodUnits.size() && period.getValue(fieldIndex)==0; ++fieldIndex) {
			smallestEmptyField = periodUnits.getFieldType(fieldIndex);
		}
		return smallestEmptyField;
	}
	
	
	
}
