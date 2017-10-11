package glug.gui.zoom;

import glug.gui.UITimeScale;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class TimelineViewportTest {


    @Test
    public void shouldMoveViewSoThatInstantIsAtRequiredLocationInViewport() {
        UITimeScale uiTimeScale = new UITimeScale();
        uiTimeScale.setFullInterval(new Interval(4000, 7000));
        uiTimeScale.setMillisecondsPerPixel(100);
        JViewport viewport = new JViewport();
        viewport.setView(viewOfSize(new Dimension(10000, 100)));
        viewport.setExtentSize(new Dimension(1000, 100));
        viewport.setViewPosition(new Point(3000, 0));

        TimelineViewport timelineViewport = new TimelineViewport(uiTimeScale, viewport);

        Instant instant = new Instant(5000);

        timelineViewport.setViewPosition(instant, 200);

        assertThat(timelineViewport.getViewportCoordinateFor(instant), equalTo(200));
    }

    private Component viewOfSize(Dimension viewSize) {
        Component view = new Canvas();
        view.setSize(viewSize);
        return view;
    }
}
