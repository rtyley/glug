package com.gu.glug.gui;

import static java.lang.Math.max;
import static java.lang.Math.min;
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

import org.joda.time.Interval;

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
	private final TimelineCursor timelineCursor;

	
	public ThreadedSystemViewComponent(UITimeScale timeScale, ThreadedSystem threadedSystem,
			TimelineCursor timelineCursor) {
		super(timeScale);
		
		setCursor(new FineCrosshairMouseCursorFactory().createFineCrosshairMouseCursor());
		this.threadedSystem = threadedSystem;
		this.timelineCursor = timelineCursor;
		turnOnToolTips();
		
		timelineCursor.install(this);
		//setDoubleBuffered(true); // benefit?
	}

	private void turnOnToolTips() {
		ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
		makeResponsive(toolTipManager);
		toolTipManager.registerComponent(this);
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
		return new Dimension(uiTimeScale.fullModelToViewLength(), threadedSystem.getNumThreads());
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;
		Rectangle clipBounds = graphics2D.getClipBounds();
		//System.out.println("Clip bounds ="+clipBounds);
		if (containsData()) {
			LogInterval visibleInterval = visibleIntervalFor(clipBounds);
			List<ThreadModel> fullThreadList = new ArrayList<ThreadModel>(threadedSystem.getThreads());
			paint(fullThreadList, minThreadIndexFor(clipBounds, fullThreadList), maxThreadIndexFor(clipBounds, fullThreadList), visibleInterval, graphics2D);
			timelineCursor.paintOn(this, graphics2D);
		}
	}

	private int maxThreadIndexFor(Rectangle clipBounds, List<ThreadModel> fullThreadList) {
		return min(clipBounds.y+clipBounds.height,fullThreadList.size()-1);
	}

	private int minThreadIndexFor(Rectangle clipBounds, List<ThreadModel> fullThreadList) {
		return min(max(clipBounds.y,0),fullThreadList.size()-1);
	}

	private void paint(List<ThreadModel> threads, int minThreadIndex, int maxThreadIndex, LogInterval visibleInterval, Graphics2D g) {
		//System.out.println("Asked to paint "+threads.size()+" "+visibleInterval);
		long startRenderTime = currentTimeMillis();
		for (int threadIndex = minThreadIndex ; threadIndex<=maxThreadIndex;++threadIndex) {
			ThreadModel threadModel = threads.get(threadIndex);
			paintThread(threadModel, threadIndex, visibleInterval, g);
			
			long expiredDuration = currentTimeMillis()-startRenderTime;
			if (expiredDuration>100) {
				System.out.println("Abandoning painting after "+expiredDuration+" ms");
				repaint(visibleInterval, threadIndex+1, maxThreadIndex);
				return;
			}
		}
		//System.out.println("duration =" + (currentTimeMillis()-startRenderTime));
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
		Interval interval = uiTimeScale.viewToModel(clipBounds);
		return new LogInterval(new LogInstant(interval.getStart().toInstant(),0),new LogInstant(interval.getEnd().toInstant(),Integer.MAX_VALUE));
	}

	private LogInstant instantFor(int graphicsX) {
		return new LogInstant( uiTimeScale.viewToModel(graphicsX),0);
	}

	public void repaint(LogInterval logInterval) {
		//System.out.println("***Want to repaint "+logInterval);
		repaint(boundsFor(logInterval, 0, threadedSystem.getThreads().size()-1));
	}
	
	private void repaint(LogInterval logInterval, int minThreadIndex, int maxThreadIndex) {
		repaint(boundsFor(logInterval, minThreadIndex, maxThreadIndex));
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

	@Override
	public boolean containsData() {
		return threadedSystem.getNumThreads()>0;
	}

}
