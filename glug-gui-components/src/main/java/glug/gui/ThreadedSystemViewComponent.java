package glug.gui;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.sort;
import glug.gui.mousecursor.FineCrosshairMouseCursorFactory;
import glug.gui.timelinecursor.TimelineCursor;
import glug.model.ThreadModel;
import glug.model.ThreadedSystem;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.ToolTipManager;

import org.joda.time.Interval;

public class ThreadedSystemViewComponent extends TimelineComponent {

	private static final long serialVersionUID = 1L;
	
	private ThreadedSystem threadedSystem;

	private final UIThreadScale threadScale;

	private final UILogTimeScale uiLogTimeScale;

	private final SwingHtmlStyleThreadReporter htmlStyleReporter = new SwingHtmlStyleThreadReporter();
	
	private final ThreadPainter threadPainter;
	
	private static final Comparator<ThreadModel> niceThreadOrderer = new Comparator<ThreadModel>() {
		@Override
		public int compare(ThreadModel t1, ThreadModel t2) {
			return NiceOrderBasedOnNumericThreadIdComparator.INSTANCE.compare(t1.getThreadId(), t2.getThreadId());
		}
	};
	
	public ThreadedSystemViewComponent(UITimeScale timeScale, UIThreadScale threadScale, ThreadedSystem threadedSystem, TimelineCursor timelineCursor) {
		super(timeScale, timelineCursor);
		this.threadScale = threadScale;
		this.threadedSystem = threadedSystem;
		uiLogTimeScale = new UILogTimeScale(timeScale);
		threadPainter = new ThreadPainter(uiLogTimeScale,threadScale);
		setCursor(new FineCrosshairMouseCursorFactory().createFineCrosshairMouseCursor());
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
	protected int getPreferredHeight() {
		return threadScale.fullModelToViewLength();
	}

	@Override
	protected void paintPopulatedComponent(Graphics2D graphics2D) {
		Rectangle clipBounds = graphics2D.getClipBounds();
		LogInterval visibleInterval = visibleIntervalFor(clipBounds);
		List<ThreadModel> fullThreadList = fullThreadList();
		paint(fullThreadList, minThreadIndexFor(clipBounds, fullThreadList), maxThreadIndexFor(clipBounds, fullThreadList), visibleInterval, graphics2D);
		getTimelineCursor().paintOn(this, graphics2D);
	}

	private List<ThreadModel> fullThreadList() {
		List<ThreadModel> fullThreadList = new ArrayList<ThreadModel>(threadedSystem.getThreads());
		sort(fullThreadList, niceThreadOrderer);
		return fullThreadList;
	}
	
	protected LogInterval visibleIntervalFor(Rectangle clipBounds) {
		Interval interval = uiTimeScale.viewToModel(clipBounds);
		return new LogInterval(new LogInstant(interval.getStart().getMillis()-1,0),new LogInstant(interval.getEnd().getMillis()+1,Integer.MAX_VALUE));
	}

	private int maxThreadIndexFor(Rectangle clipBounds, List<ThreadModel> fullThreadList) {
		return min(clipBounds.y+clipBounds.height,fullThreadList.size()-1);
	}

	private int minThreadIndexFor(Rectangle clipBounds, List<ThreadModel> fullThreadList) {
		return min(max(threadScale.viewToModelThreadIndex(clipBounds.y),0),fullThreadList.size()-1);
	}

	private void paint(List<ThreadModel> threads, int minThreadIndex, int maxThreadIndex, LogInterval visibleInterval, Graphics2D g) {
		long startRenderTime = currentTimeMillis();
		for (int threadIndex = minThreadIndex ; threadIndex<=maxThreadIndex;++threadIndex) {
			ThreadModel threadModel = threads.get(threadIndex);
			threadPainter.paintThread(threadModel, threadIndex, visibleInterval, g);
			
			long expiredDuration = currentTimeMillis()-startRenderTime;
			if (expiredDuration>100) {
				// System.out.println("Abandoning painting after "+expiredDuration+" ms");
				repaint(visibleInterval, threadIndex+1, maxThreadIndex);
				return;
			}
		}
		//System.out.println("duration =" + (currentTimeMillis()-startRenderTime));
	}

	private LogInstant instantFor(int graphicsX) {
		return new LogInstant(uiTimeScale.viewToModel(graphicsX), 0);
	}
	
	private void repaint(LogInterval logInterval, int minThreadIndex, int maxThreadIndex) {
		repaint(boundsFor(logInterval, minThreadIndex, maxThreadIndex));
	}

	private Rectangle boundsFor(LogInterval logInterval, int threadSetStartIndex, int threadSetEndIndex) {
		int x= uiLogTimeScale.modelToView(logInterval.getStart());
		int width=uiLogTimeScale.modelToView(logInterval.getEnd()) - x;
		
		int threadSetStartY = threadScale.modelThreadIndexToView(threadSetStartIndex);
		int threadSetEndY = threadScale.modelThreadIndexToView(threadSetEndIndex+1);
		
		return new Rectangle(x,threadSetStartY, width, threadSetEndY - threadSetStartY);
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
		return htmlStyleReporter.htmlSyledReportFor(thread, instant);
	}

	private ThreadModel threadFor(Point point) {
		List<ThreadModel> threads = fullThreadList();
		int threadIndex = threadScale.viewToModelThreadIndex(point);
		if (threadIndex >= 0 && threadIndex < threads.size()) {
			return threads.get(threadIndex);
		}
		return null;
	}

	@Override
	public boolean containsData() {
		return !threadedSystem.getThreads().isEmpty();
	}

}
