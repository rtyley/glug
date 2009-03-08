package com.gu.glug.gui;

import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;
import static java.lang.System.currentTimeMillis;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.Map.Entry;

import javax.swing.ToolTipManager;

import com.gu.glug.gui.model.LogarithmicBoundedRange;
import com.gu.glug.model.SignificantInterval;
import com.gu.glug.model.ThreadModel;
import com.gu.glug.model.ThreadedSystem;
import com.gu.glug.model.time.LogInstant;
import com.gu.glug.model.time.LogInterval;
import com.gu.glug.parser.logmessages.IntervalTypeDescriptor;

/**
 * 
 * @author roberto
 */
public class ThreadedSystemViewComponent extends TimelineComponent {

	private static final long serialVersionUID = 1L;
	private ThreadedSystem threadedSystem;
	LogarithmicBoundedRange logarithmicBoundedRange;
	private final TimelineCursor timelineCursor;

	
	public ThreadedSystemViewComponent(ThreadedSystem threadedSystem,
			TimelineCursor timelineCursor) {
		this.threadedSystem = threadedSystem;
		this.timelineCursor = timelineCursor;
		cacheIntervalCoveredByAllThreads();
		ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
		makeResponsive(toolTipManager);
		toolTipManager.registerComponent(this);
		timelineCursor.install(this);
	}

	private void makeResponsive(ToolTipManager toolTipManager) {
		toolTipManager.setInitialDelay(20);
		toolTipManager.setReshowDelay(10);
		toolTipManager.setDismissDelay(10000);
	}

	@Override
	public Dimension getPreferredSize() {
		if (!containsData()) {
			return super.getPreferredSize();
		}
		int requiredWidth = (int) ceil(getDrawDistanceFor(intervalCoveredByAllThreads));
		return new Dimension(requiredWidth, threadedSystem.getNumThreads());
	}



	@Override
	LogInterval getEntireInterval() {
		return getIntervalCoveredByAllThreads(true);
	}

	public LogInterval getIntervalCoveredByAllThreads(boolean update) {
		if (update) {
			cacheIntervalCoveredByAllThreads();
		}
		return intervalCoveredByAllThreads;
	}

	private void cacheIntervalCoveredByAllThreads() {
		intervalCoveredByAllThreads = threadedSystem.getIntervalCoveredByAllThreads();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;
		Rectangle clipBounds = graphics2D.getClipBounds();
		//System.out.println("Clip bounds ="+clipBounds);
		cacheIntervalCoveredByAllThreads();
		if (containsData()) {
			LogInterval visibleInterval = visibleIntervalFor(clipBounds);
			List<ThreadModel> fullThreadList = new ArrayList<ThreadModel>(threadedSystem.getThreads());
			paint(fullThreadList, minThreadIndexFor(clipBounds, fullThreadList), maxThreadIndexFor(clipBounds, fullThreadList), visibleInterval, graphics2D);
			timelineCursor.paintOn(this, graphics2D);
		}
	}

	private int maxThreadIndexFor(Rectangle clipBounds, List<ThreadModel> fullThreadList) {
		return min(clipBounds.y+clipBounds.height,fullThreadList.size());
	}

	private int minThreadIndexFor(Rectangle clipBounds, List<ThreadModel> fullThreadList) {
		return min(max(clipBounds.y,0),fullThreadList.size());
	}

	private void paint(List<ThreadModel> threads, int minThreadIndex, int maxThreadIndex, LogInterval visibleInterval, Graphics2D g) {
		System.out.println("Asked to paint "+threads.size()+" "+visibleInterval);
		long startRenderTime = currentTimeMillis();
		for (int threadIndex = minThreadIndex ; threadIndex<maxThreadIndex;++threadIndex) {
			ThreadModel threadModel = threads.get(threadIndex);
			paintThread(threadModel, threadIndex, visibleInterval, g);
			
			long expiredDuration = currentTimeMillis()-startRenderTime;
			if (expiredDuration>100) {
				System.out.println("quitting with"+expiredDuration);
				//repaint(logInterval)
				return;
			}
		}
		System.out.println("duration =" + (currentTimeMillis()-startRenderTime));
	}

	private void paintThread(ThreadModel threadModel, int threadIndex, LogInterval visibleInterval, Graphics2D g) {
		for (Entry<IntervalTypeDescriptor, Collection<SignificantInterval>> blah : threadModel
				.getSignificantIntervalsFor(visibleInterval).entrySet()) {
			IntervalTypeDescriptor intervalTypeDescriptor = blah.getKey();
			g.setColor(intervalTypeDescriptor.getColour());

			Collection<SignificantInterval> sigInts = blah.getValue();
			// System.out.println("size="+size);
			for (SignificantInterval significantInterval : sigInts) {
				LogInterval aa = significantInterval.getLogInterval();
				
				LogInterval visibleIntervalOfLine = aa.overlap(visibleInterval);
				if (visibleIntervalOfLine!=null) {
					int startX = graphicsXFor(visibleIntervalOfLine.getStart().getRecordedInstant());
					int endX = graphicsXFor(visibleIntervalOfLine.getEnd().getRecordedInstant());
					g.drawLine(startX,threadIndex,endX,threadIndex);
				}
			}
		}
	}

	private LogInterval visibleIntervalFor(Rectangle clipBounds) {
		return new LogInterval(instantFor(clipBounds.getMinX()),
				instantFor(clipBounds.getMaxX()));
	}

	private double getDrawDistanceFor(LogInterval interval) {
		return interval.toDurationMillis() / millisecondsPerPixel;
	}

	private LogInstant instantFor(double graphicsX) {
		return new LogInstant( intervalCoveredByAllThreads.getStart().getRecordedInstant().plus(
				round(graphicsX * millisecondsPerPixel)),0);
	}

	public void repaint(LogInterval logInterval) {
		System.out.println("***Want to repaint "+logInterval);
		cacheIntervalCoveredByAllThreads();
		repaint(boundsFor(logInterval, 0, threadedSystem.getThreads().size()));
//		repaint(graphicsXFor(interval.getStart().toInstant()) - 1, 0,
//				graphicsXFor(interval.getEnd().toInstant()) + 1, threadedSystem
//						.getNumThreads());
	}

	private Rectangle boundsFor(LogInterval logInterval, int threadSetStartIndex, int threadSetEndIndex) {
		int x=graphicsXFor(logInterval.getStart().getRecordedInstant());
		int width=graphicsXFor(logInterval.getEnd().getRecordedInstant()) - x;
		return new Rectangle(x,threadSetStartIndex,width,threadSetEndIndex);
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		if (!containsData()) {
			return null;
		}
		ThreadModel thread = threadFor(event.getPoint());
		if (thread == null) {
			return null;
		}
		LogInstant instant = instantFor(event.getX());
		SortedSet<SignificantInterval> significantIntervalsForInstant = thread.getSignificantIntervalsFor(instant);
		if (significantIntervalsForInstant.isEmpty()) {
			return null;
		}
		return "<html>" + significantIntervalsForInstant.toString() + "</html>";
	}

	private ThreadModel threadFor(Point point) {

		ArrayList<ThreadModel> threads = new ArrayList<ThreadModel>(
				threadedSystem.getThreads());
		int threadIndex = point.y;
		if (threadIndex >= 0 && threadIndex < threads.size()) {
			return threads.get(threadIndex);
		}
		return null;
	}

	@Override
	public LogInstant getLogInstantFor(Point point) {
		return instantFor(point.x);
	}

	@Override
	public TimelineCursor getTimelineCursor() {
		return timelineCursor;
	}

	@Override
	Rectangle getViewFor(LogInstant logInstant) {
		return new Rectangle(graphicsXFor(logInstant.getRecordedInstant()), 0, 0,
				getHeight());
	}

}
