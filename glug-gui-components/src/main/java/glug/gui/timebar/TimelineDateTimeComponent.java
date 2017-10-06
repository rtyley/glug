package glug.gui.timebar;

import glug.gui.UITimeScale;
import org.threeten.extra.Interval;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;

import static glug.gui.timebar.Tick.tick;
import static java.awt.Color.WHITE;
import static java.awt.Font.PLAIN;
import static java.awt.Font.SANS_SERIF;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.lang.Math.*;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.ChronoField.*;


public class TimelineDateTimeComponent extends JComponent {

    int minTickPixelSpacing = 3;
    int maxTickPixelSpacing = 160;
    private TickSet availableTicks = new TickSet(
            tick(1, MONTH_OF_YEAR, ofPattern("YYYY-MM")),
            tick(1, DAY_OF_MONTH, ofPattern("YYYY-MM-dd")),
            tick(4, HOUR_OF_DAY, ofPattern("YYYY-MM-dd HH:mm")), tick(1, HOUR_OF_DAY, ofPattern("HH:mm")),
            tick(10, MINUTE_OF_HOUR, ofPattern("HH:mm")), tick(5, MINUTE_OF_HOUR, ofPattern("HH:mm")), tick(1, MINUTE_OF_HOUR, ofPattern("HH:mm")),
            tick(10, SECOND_OF_MINUTE, ofPattern("HH:mm:ss")), tick(5, SECOND_OF_MINUTE, ofPattern("HH:mm:ss")), tick(1, SECOND_OF_MINUTE, ofPattern("HH:mm:ss")),
            tick(100, MILLI_OF_SECOND, ofPattern("HH:mm:ss.S")), tick(10, MILLI_OF_SECOND, ofPattern("HH:mm:ss.SS")), tick(1, MILLI_OF_SECOND, ofPattern("HH:mm:ss.SSS")));

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

    public void setTimeZone(ZoneId zoneId) {
        availableTicks = availableTicks.with(zoneId);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Integer.MAX_VALUE >>> 2, 32);
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
        Map<ZonedDateTime, Tick> tickMap = new HashMap<>();
        Map<Tick, Float> tickWeight = new HashMap<Tick, Float>();
        for (Tick tick : availableTicks.values()) {
            int pixelsForTickDuration = timeScale.modelDurationToViewPixels(tick.getInterval().getDuration());
            float neif = (pixelsForTickDuration - minTickPixelSpacing) / (maxTickPixelSpacing * 1.2f - minTickPixelSpacing);
            float proportionOfRange = max(0, min(1, neif));
            if (tick.getInterval().getValue() == 1) {
                proportionOfRange = (float) pow(proportionOfRange, 0.7);
            }
            tickWeight.put(tick, proportionOfRange);
            Iterator<ZonedDateTime> tickIterator = tick.getInterval().ticksFor(visibleInterval, ZoneId.systemDefault());
            while (tickIterator.hasNext()) {
                ZonedDateTime tickDateTime = tickIterator.next();
                tickMap.put(tickDateTime, tick);
            }
        }
        paintTicksFor(tickMap, tickWeight, graphics2D);
    }

    private void paintTicksFor(Map<ZonedDateTime, Tick> tickMap,
                               Map<Tick, Float> tickWeight, Graphics2D graphics2D) {
        int tickHeight;
        int bottom = getHeight() - 1;
        graphics2D.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        Font baseFont = new Font(SANS_SERIF, PLAIN, 16);
        for (Entry<ZonedDateTime, Tick> entry : tickMap.entrySet()) {
            Tick tick = entry.getValue();
            float proportionOfRange = tickWeight.get(tick);
            int col = round(255 * (1f - proportionOfRange));
            graphics2D.setColor(new Color(col, col, col));
            float punchSize = (float) (16f * exp(proportionOfRange - 1));
            tickHeight = (int) round(punchSize);
            ZonedDateTime tickDateTime = entry.getKey();
            int graphicsX = timeScale.modelToView(tickDateTime.toInstant());
            graphics2D.drawLine(graphicsX, bottom, graphicsX, bottom - tickHeight);
            if (proportionOfRange > 0.3) {
                Font tickFont = baseFont.deriveFont(punchSize - 1);
                graphics2D.setFont(tickFont);
                TextLayout textLayout = new TextLayout(tick.format(tickDateTime), tickFont, graphics2D.getFontRenderContext());
                textLayout.draw(graphics2D, (float) (-(textLayout.getBounds().getWidth() / 2) + graphicsX), (float) bottom - tickHeight - 1);
            }
        }
    }

    private NavigableMap<Duration, Tick> tickIntervalsAtCurrentScale() {
        Duration approxGoodMinorTickDuration = timeScale.viewPixelsToModelDuration(minTickPixelSpacing);
        Duration approxGoodMajorTickDuration = timeScale.viewPixelsToModelDuration(maxTickPixelSpacing);

        return availableTicks.forRange(approxGoodMinorTickDuration, approxGoodMajorTickDuration);
    }

}
