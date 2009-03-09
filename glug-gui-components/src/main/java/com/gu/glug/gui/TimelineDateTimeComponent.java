package com.gu.glug.gui;

import static com.gu.glug.gui.TickInterval.tick;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.lang.Math.exp;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static org.joda.time.DateTimeFieldType.dayOfMonth;
import static org.joda.time.DateTimeFieldType.hourOfDay;
import static org.joda.time.DateTimeFieldType.millisOfSecond;
import static org.joda.time.DateTimeFieldType.minuteOfHour;
import static org.joda.time.DateTimeFieldType.monthOfYear;
import static org.joda.time.DateTimeFieldType.secondOfMinute;
import static org.joda.time.format.DateTimeFormat.forPattern;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Map.Entry;

import javax.swing.JComponent;

import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimelineDateTimeComponent extends JComponent {
	
	int minTickPixelSpacing = 3;
	int maxTickPixelSpacing = 160;
	private static TickIntervalSet tickIntervalSet = new TickIntervalSet(
			tick(1,DateTimeFieldType.yearOfCentury(),forPattern("YYYY")),
			tick(1,monthOfYear(),forPattern("YYYY-MM")),
			tick(1,dayOfMonth(),forPattern("YYYY-MM-dd")),
			tick(4,hourOfDay(),forPattern("YYYY-MM-dd HH:mm")), tick(1,hourOfDay(),forPattern("HH:mm")),
			tick(10,minuteOfHour(),forPattern("HH:mm")), tick(5,minuteOfHour(),forPattern("HH:mm")), tick(1,minuteOfHour(),forPattern("HH:mm")),
			tick(10,secondOfMinute(),forPattern("HH:mm:ss")), tick(5,secondOfMinute(),forPattern("HH:mm:ss")), tick(1,secondOfMinute(),forPattern("HH:mm:ss")),
			tick(100,millisOfSecond(),forPattern("HH:mm:ss.S")),tick(10,millisOfSecond(),forPattern("HH:mm:ss.SS")),tick(1,millisOfSecond(),forPattern("HH:mm:ss.SSS")));
	
	private static final long serialVersionUID = 1L;
	
	private final UITimeScale timeScale;

	public TimelineDateTimeComponent(UITimeScale timeScale) {
		this.timeScale = timeScale;
		//setSize(getPreferredSize());
		setDoubleBuffered(true);
		setBackground(WHITE);
		setOpaque(true);
		timeScale.addChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				//setSize(getPreferredSize());
				if (evt.getPropertyName().equals("millisecondsPerPixel")) {
					repaint();
				}
			}
		});
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
		Map<DateTime, TickInterval> tickMap = new HashMap<DateTime, TickInterval>();
		Map<TickInterval, Float> tickWeight = new HashMap<TickInterval, Float>();
		for (TickInterval tickInterval : periodRange.values()) {
			Iterator<DateTime> tickIterator = tickInterval.ticksFor(visibleInterval);
			int pixelsForTickDuration = timeScale.modelDurationToViewPixels(tickInterval.getDuration());
			float neif =(pixelsForTickDuration - minTickPixelSpacing)/(maxTickPixelSpacing*1.2f - minTickPixelSpacing);
			float proportionOfRange = max(0,min(1,neif));
			if (tickInterval.getValue()==1) {
				proportionOfRange=(float) pow(proportionOfRange, 0.7);
			}
			tickWeight.put(tickInterval, proportionOfRange);
			while (tickIterator.hasNext()) {
				DateTime tickDateTime = tickIterator.next();
				tickMap.put(tickDateTime, tickInterval);
			}
		}
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm:ss.SSS");
		Font baseFont = new Font(Font.SANS_SERIF, Font.PLAIN,16);
		for (Entry<DateTime, TickInterval> entry : tickMap.entrySet()) {
			TickInterval tickInterval = entry.getValue();
			float proportionOfRange = tickWeight.get(tickInterval);
			int col=round(255*(1f-proportionOfRange));
			g.setColor(new Color(col,col,col));
			tickHeight = (int) round(16f*exp(proportionOfRange-1));
			DateTime tickDateTime = entry.getKey();
			int graphicsX = timeScale.modelToView(tickDateTime.toInstant());
			graphics2D.drawLine(graphicsX, bottom, graphicsX, bottom-tickHeight);
			if (proportionOfRange>0.3) {
				Font tickFont = baseFont.deriveFont((float)tickHeight-1);
				String myString=tickInterval.format(tickDateTime);
				graphics2D.setFont(tickFont);
				TextLayout textLayout = new TextLayout(myString,tickFont,graphics2D.getFontRenderContext());
				textLayout.draw(graphics2D, (float)(-(textLayout.getBounds().getWidth()/2)+graphicsX),(float) bottom-tickHeight-1);
			}
		}
	}

	private NavigableMap<Duration, TickInterval> tickIntervalsAtCurrentScale() {
		
		Duration approxGoodMinorTickDuration = timeScale.viewPixelsToModelDuration(minTickPixelSpacing);
		Duration approxGoodMajorTickDuration = timeScale.viewPixelsToModelDuration(maxTickPixelSpacing);
		
		return tickIntervalSet.rangeFor(approxGoodMinorTickDuration,approxGoodMajorTickDuration);
	}
	
}
