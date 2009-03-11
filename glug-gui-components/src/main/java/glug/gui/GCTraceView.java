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
		Interval interval = uiTimeScale.viewToModel(clipBounds);
		
		Instant startOfVisibleInterval = interval.getStart().toInstant();
		Instant jvmRestartPrecedingStartOfVisibleInterval = threadedSystem.getUptime().startPreceding(startOfVisibleInterval);
		graphics2D.setColor(BLACK);
		for (GCActivity activity : fullGCActivitySet) {
			Instant activityStartInstant = jvmRestartPrecedingStartOfVisibleInterval.plus(round(activity.getStartSec()*1000));
			Duration activityDuration = new Duration(round(activity.getDurationSec()*1000));
			graphics2D.fillRect(uiTimeScale.modelToView(activityStartInstant), 0, uiTimeScale.modelDurationToViewPixels(activityDuration), 100);
		}
		
	}


	private int indexOfActivityWithName(String name) {
		return gcTrace.getGCActivityNames().indexOf(name);
	}
	
	

}
