package glug.gui.zoom;

import glug.gui.UITimeScale;
import org.threeten.extra.Interval;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;

public class TimelineViewport {

    private final UITimeScale uiTimeScale;
    private final JViewport viewport;


    public TimelineViewport(UITimeScale uiTimeScale, JViewport viewport) {
        this.uiTimeScale = uiTimeScale;
        this.viewport = viewport;
    }

    public UITimeScale getUITimeScale() {
        return uiTimeScale;
    }

    public int getViewportCoordinateFor(Instant instant) {
        int viewCoordinate = uiTimeScale.modelToView(instant);
        return viewCoordinate - viewport.getViewPosition().x;
    }

    public void setViewPosition(Instant instant, int viewportPositionForInstant) {
        int requiredLeftBoundInViewCoords = uiTimeScale.modelToView(instant) - viewportPositionForInstant;
        viewport.setViewPosition(new Point(requiredLeftBoundInViewCoords, viewport.getViewPosition().y));
    }

    public Interval getVisibleInterval() {
        return uiTimeScale.viewToModel(viewport.getViewRect());
    }

}
