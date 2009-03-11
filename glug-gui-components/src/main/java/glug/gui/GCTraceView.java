package glug.gui;

import static java.awt.Color.BLACK;
import static java.lang.Math.round;
import gchisto.gcactivity.GCActivity;
import gchisto.gcactivity.GCActivitySet;
import gchisto.gctrace.GCTrace;
import glug.model.ThreadedSystem;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;

public class GCTraceView extends TimelineComponent {

	private static final long serialVersionUID = 1L;
	private final GCTrace gcTrace;
	private GCActivitySet fullGCActivitySet;
	private final ThreadedSystem threadedSystem;

	public GCTraceView(GCTrace gcTrace, UITimeScale uiTimeScale, ThreadedSystem threadedSystem, TimelineCursor timelineCursor) {
		super(uiTimeScale, timelineCursor);
		this.gcTrace = gcTrace;
		this.threadedSystem = threadedSystem;
		
		fullGCActivitySet = gcTrace.get(indexOfActivityWithName("Full GC"));
	}

	@Override
	public boolean containsData() {
		return true;
	}

	@Override
	int getPreferredHeight() {
		return 100;
	}

	@Override
	void paintPopulatedComponent(Graphics2D graphics2D) {
		Rectangle clipBounds = graphics2D.getClipBounds();
		Interval visibleInterval = uiTimeScale.viewToModel(clipBounds);
		
		Instant startOfVisibleInterval = visibleInterval.getStart().toInstant();
		Instant jvmRestartPrecedingStartOfVisibleInterval = threadedSystem.uptime().startPreceding(startOfVisibleInterval);
		graphics2D.setColor(BLACK);
		for (GCActivity activity : gcTrace.getAllGCActivities()) {
			Instant activityStartInstant = jvmRestartPrecedingStartOfVisibleInterval.plus(round(activity.getStartSec()*1000));
			Duration activityDuration = new Duration(round(activity.getDurationSec()*1000));
			Interval activityInterval = new Interval(activityStartInstant,activityDuration);
			Interval visibleActivityInterval = visibleInterval.overlap(activityInterval);
			if (visibleActivityInterval!=null) {
				float colVal = (float) (100-activity.getOverheadPerc());
				graphics2D.setColor(new Color(colVal,colVal,colVal));
				graphics2D.fillRect(
						uiTimeScale.modelToView(visibleActivityInterval.getStart().toInstant()),
						0,
						uiTimeScale.modelDurationToViewPixels(visibleActivityInterval.toDuration()), 100);
			}
		}
		
	}


	private int indexOfActivityWithName(String name) {
		return gcTrace.getGCActivityNames().indexOf(name);
	}
	
	

}
