package glug.gui.timebar;

import static glug.gui.timebar.Tick.tick;
import static java.awt.Color.WHITE;
import static java.awt.Font.PLAIN;
import static java.awt.Font.SANS_SERIF;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
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
import static org.joda.time.DateTimeFieldType.yearOfCentury;
import static org.joda.time.format.DateTimeFormat.forPattern;
import glug.gui.UITimeScale;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Interval;

public class TimelineDateTimeComponent extends JComponent {
	
	int minTickPixelSpacing = 3;
	int maxTickPixelSpacing = 160;
	private static TickSet availableTicks = new TickSet(
			tick(1,yearOfCentury(),forPattern("YYYY")),
			tick(1,monthOfYear(),forPattern("YYYY-MM")),
			tick(1,dayOfMonth(),forPattern("YYYY-MM-dd")),
			tick(4,hourOfDay(),forPattern("YYYY-MM-dd HH:mm")), tick(1,hourOfDay(),forPattern("HH:mm")),
			tick(10,minuteOfHour(),forPattern("HH:mm")), tick(5,minuteOfHour(),forPattern("HH:mm")), tick(1,minuteOfHour(),forPattern("HH:mm")),
			tick(10,secondOfMinute(),forPattern("HH:mm:ss")), tick(5,secondOfMinute(),forPattern("HH:mm:ss")), tick(1,secondOfMinute(),forPattern("HH:mm:ss")),
			tick(100,millisOfSecond(),forPattern("HH:mm:ss.S")),tick(10,millisOfSecond(),forPattern("HH:mm:ss.SS")),tick(1,millisOfSecond(),forPattern("HH:mm:ss.SSS")))
	.with(DateTimeZone.forID("Europe/London"));
	
	private static final long serialVersionUID = 1L;
	
	private final UITimeScale timeScale;

	public TimelineDateTimeComponent(UITimeScale timeScale) {
		this.timeScale = timeScale;
		setSize(getPreferredSize());
		setDoubleBuffered(true);
		setBackground(WHITE);
		setOpaque(true);
		timeScale.addChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("millisecondsPerPixel")) {
					repaint();
				}
			}
		});
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Integer.MAX_VALUE>>>2, 32);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2D = (Graphics2D) g;
		Rectangle clipBounds = graphics2D.getClipBounds();
		fillBackgroundOf(clipBounds, graphics2D);
		Interval visibleInterval = timeScale.viewToModel(clipBounds);
		paintTicksFor(visibleInterval, graphics2D);
	}

	private void fillBackgroundOf(Rectangle clipBounds, Graphics2D graphics2D) {
		graphics2D.setColor(getBackground());
		graphics2D.fill(clipBounds);
	}

	private void paintTicksFor(Interval visibleInterval, Graphics2D graphics2D) {
		NavigableMap<Duration, Tick> availableTicks = tickIntervalsAtCurrentScale();
		Map<DateTime, Tick> tickMap = new HashMap<DateTime, Tick>();
		Map<Tick, Float> tickWeight = new HashMap<Tick, Float>();
		for (Tick tick : availableTicks.values()) {
			int pixelsForTickDuration = timeScale.modelDurationToViewPixels(tick.getInterval().getDuration());
			float neif =(pixelsForTickDuration - minTickPixelSpacing)/(maxTickPixelSpacing*1.2f - minTickPixelSpacing);
			float proportionOfRange = max(0,min(1,neif));
			if (tick.getInterval().getValue()==1) {
				proportionOfRange=(float) pow(proportionOfRange, 0.7);
			}
			tickWeight.put(tick, proportionOfRange);
			Iterator<DateTime> tickIterator = tick.getInterval().ticksFor(visibleInterval);
			while (tickIterator.hasNext()) {
				DateTime tickDateTime = tickIterator.next();
				tickMap.put(tickDateTime, tick);
			}
		}
		paintTicksFor(tickMap, tickWeight, graphics2D);
	}

	private void paintTicksFor(Map<DateTime, Tick> tickMap,
			Map<Tick, Float> tickWeight, Graphics2D graphics2D) {
		int tickHeight;
		int bottom = getHeight()-1;
		graphics2D.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		Font baseFont = new Font(SANS_SERIF, PLAIN,16);
		for (Entry<DateTime, Tick> entry : tickMap.entrySet()) {
			Tick tick = entry.getValue();
			float proportionOfRange = tickWeight.get(tick);
			int col=round(255*(1f-proportionOfRange));
			graphics2D.setColor(new Color(col,col,col));
			float punchSize = (float) (16f*exp(proportionOfRange-1));
			tickHeight = (int) round(punchSize);
			DateTime tickDateTime = entry.getKey();
			int graphicsX = timeScale.modelToView(tickDateTime.toInstant());
			graphics2D.drawLine(graphicsX, bottom, graphicsX, bottom-tickHeight);
			if (proportionOfRange>0.3) {
				Font tickFont = baseFont.deriveFont(punchSize-1);
				graphics2D.setFont(tickFont);
				TextLayout textLayout = new TextLayout(tick.format(tickDateTime),tickFont,graphics2D.getFontRenderContext());
				textLayout.draw(graphics2D, (float)(-(textLayout.getBounds().getWidth()/2)+graphicsX),(float) bottom-tickHeight-1);
			}
		}
	}

	private NavigableMap<Duration, Tick> tickIntervalsAtCurrentScale() {
		Duration approxGoodMinorTickDuration = timeScale.viewPixelsToModelDuration(minTickPixelSpacing);
		Duration approxGoodMajorTickDuration = timeScale.viewPixelsToModelDuration(maxTickPixelSpacing);
		
		return availableTicks.forRange(approxGoodMinorTickDuration,approxGoodMajorTickDuration);
	}
	
}
