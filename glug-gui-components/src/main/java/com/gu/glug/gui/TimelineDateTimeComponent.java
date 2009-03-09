package com.gu.glug.gui;

import static com.gu.glug.gui.TickInterval.tick;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.lang.Math.exp;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import static org.joda.time.DateTimeFieldType.dayOfMonth;
import static org.joda.time.DateTimeFieldType.hourOfDay;
import static org.joda.time.DateTimeFieldType.millisOfSecond;
import static org.joda.time.DateTimeFieldType.minuteOfHour;
import static org.joda.time.DateTimeFieldType.secondOfMinute;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.NavigableMap;

import javax.swing.JComponent;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

public class TimelineDateTimeComponent extends JComponent {
	
	int minTickPixelSpacing = 3;
	int maxTickPixelSpacing = 160;

	private static TickIntervalSet tickIntervalSet = new TickIntervalSet(
			tick(1,dayOfMonth()),
			tick(4,hourOfDay()), tick(1,hourOfDay()),
			tick(10,minuteOfHour()),   tick(5,minuteOfHour()),   tick(1,minuteOfHour()),
			tick(10,secondOfMinute()), tick(5,secondOfMinute()), tick(1,secondOfMinute()),
			tick(100,millisOfSecond()),tick(10,millisOfSecond()),tick(1,millisOfSecond()));
	
	private static final long serialVersionUID = 1L;
	
	private final UITimeScale timeScale;

	public TimelineDateTimeComponent(UITimeScale timeScale) {
		this.timeScale = timeScale;
		setSize(getPreferredSize());
		setBackground(WHITE);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(timeScale.fullModelToViewLength(), 32);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2D = (Graphics2D) g;
		Rectangle clipBounds = graphics2D.getClipBounds();
		graphics2D.setColor(getBackground());
		graphics2D.fill(clipBounds);
		NavigableMap<Duration, TickInterval> periodRange = tickIntervalsAtCurrentScale();
		int bottom = getHeight()-1;
		graphics2D.setColor(BLACK);
		Interval visibleInterval = timeScale.viewToModel(clipBounds);
		int tickHeight = 1;
		for (TickInterval tickInterval : periodRange.values()) {
			int pixelsForTickDuration = timeScale.modelDurationToViewPixels(tickInterval.getDuration());
			float neif =(pixelsForTickDuration - minTickPixelSpacing)/((float)(maxTickPixelSpacing - minTickPixelSpacing));
			float proportionOfRange = max(0,min(1,neif));
			if (tickInterval.getValue()==1) {
				proportionOfRange=(float) pow(proportionOfRange, 0.7);
			}
			int col=round(255*(1f-proportionOfRange));
			g.setColor(new Color(col,col,col));
			tickHeight = (int) round(16f*exp(proportionOfRange-1));
			Iterator<DateTime> tickIterator = tickInterval.ticksFor(visibleInterval);
			while (tickIterator.hasNext()) {
				DateTime tickDateTime = tickIterator.next();
				int graphicsX = timeScale.modelToView(tickDateTime.toInstant());
				graphics2D.drawLine(graphicsX, bottom, graphicsX, bottom-tickHeight);
			}
			//tickHeight+=2;
		}
	}

	private NavigableMap<Duration, TickInterval> tickIntervalsAtCurrentScale() {
		
		Duration approxGoodMinorTickDuration = timeScale.viewPixelsToModelDuration(minTickPixelSpacing);
		Duration approxGoodMajorTickDuration = timeScale.viewPixelsToModelDuration(maxTickPixelSpacing);
		
		return tickIntervalSet.rangeFor(approxGoodMinorTickDuration,approxGoodMajorTickDuration);
	}
	
}
