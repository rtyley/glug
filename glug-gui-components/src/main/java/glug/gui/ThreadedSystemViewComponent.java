package glug.gui;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;
import static java.lang.System.currentTimeMillis;
import glug.gui.mousecursor.FineCrosshairMouseCursorFactory;
import glug.gui.timelinecursor.TimelineCursor;
import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.ThreadedSystem;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;
import glug.parser.logmessages.IntervalTypeDescriptor;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.ToolTipManager;

public class ThreadedSystemViewComponent extends TimelineComponent {

	private static final long serialVersionUID = 1L;
	
	private ThreadedSystem threadedSystem;

	private SwingHtmlStyleThreadReporter htmlStyleReporter = new SwingHtmlStyleThreadReporter();

	private int threadGraphicsHeight = 4;
	
	public ThreadedSystemViewComponent(UITimeScale timeScale, ThreadedSystem threadedSystem, TimelineCursor timelineCursor) {
		super(timeScale, timelineCursor);
		this.threadedSystem = threadedSystem;
		
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
		return threadGraphicsHeight * threadedSystem.getNumThreads();
	}

	@Override
	protected void paintPopulatedComponent(Graphics2D graphics2D) {
		Rectangle clipBounds = graphics2D.getClipBounds();
		LogInterval visibleInterval = visibleIntervalFor(clipBounds);
		List<ThreadModel> fullThreadList = new ArrayList<ThreadModel>(threadedSystem.getThreads());
		paint(fullThreadList, minThreadIndexFor(clipBounds, fullThreadList), maxThreadIndexFor(clipBounds, fullThreadList), visibleInterval, graphics2D);
		getTimelineCursor().paintOn(this, graphics2D);
	}

	private int maxThreadIndexFor(Rectangle clipBounds, List<ThreadModel> fullThreadList) {
		return min(clipBounds.y+clipBounds.height,fullThreadList.size()-1);
	}

	private int minThreadIndexFor(Rectangle clipBounds, List<ThreadModel> fullThreadList) {
		return min(max(clipBounds.y/threadGraphicsHeight ,0),fullThreadList.size()-1);
	}

	private void paint(List<ThreadModel> threads, int minThreadIndex, int maxThreadIndex, LogInterval visibleInterval, Graphics2D g) {
		long startRenderTime = currentTimeMillis();
		for (int threadIndex = minThreadIndex ; threadIndex<=maxThreadIndex;++threadIndex) {
			ThreadModel threadModel = threads.get(threadIndex);
			paintThread(threadModel, threadIndex, visibleInterval, g);
			
			long expiredDuration = currentTimeMillis()-startRenderTime;
			if (expiredDuration>100) {
				// System.out.println("Abandoning painting after "+expiredDuration+" ms");
				repaint(visibleInterval, threadIndex+1, maxThreadIndex);
				return;
			}
		}
		//System.out.println("duration =" + (currentTimeMillis()-startRenderTime));
	}

	private void paintThread(ThreadModel threadModel, int threadIndex, LogInterval visibleInterval, Graphics2D g) {
		int durationFor1Pixel = (int)round(uiTimeScale.getMillisecondsPerPixel());
		int threadGraphicsY = graphicsYFor(threadIndex);
		
		for (Entry<IntervalTypeDescriptor, Collection<SignificantInterval>> blah : threadModel
				.getSignificantIntervalsFor(visibleInterval).entrySet()) {
			IntervalTypeDescriptor intervalTypeDescriptor = blah.getKey();
			g.setColor(intervalTypeDescriptor.getColour());

			Collection<SignificantInterval> sigInts = blah.getValue();
			
			LogInterval visibleIntervalToPlot = null;
			
			for (SignificantInterval significantInterval : sigInts) {
				LogInterval visibleIntervalOfCurrentSigInt = significantInterval.getLogInterval().overlap(visibleInterval);
				if (visibleIntervalOfCurrentSigInt!=null) {
					if (visibleIntervalToPlot==null) {
						visibleIntervalToPlot = visibleIntervalOfCurrentSigInt;
					} else if (close(visibleIntervalToPlot, durationFor1Pixel, visibleIntervalOfCurrentSigInt)) {
						visibleIntervalToPlot = visibleIntervalToPlot.union(visibleIntervalOfCurrentSigInt);
					} else {
						plotBlock(visibleIntervalToPlot, threadGraphicsY, g); // finish with the old block
						visibleIntervalToPlot = visibleIntervalOfCurrentSigInt;  // start the new block
					}
				}
			}
			if (visibleIntervalToPlot != null) {
				plotBlock(visibleIntervalToPlot, threadGraphicsY, g);
			}
			
		}
	}

	private boolean close(LogInterval visibleIntervalToPlot, int durationFor1Pixel, LogInterval visibleIntervalOfCurrentSigInt) {
		return (visibleIntervalOfCurrentSigInt.getStart().getMillis()-visibleIntervalToPlot.getEnd().getMillis())<durationFor1Pixel;
	}

	private void plotBlock(LogInterval visibleIntervalOfLine, int threadGraphicsY, Graphics2D g) {
		int startX = graphicsXFor(visibleIntervalOfLine.getStart().getRecordedInstant());
		int endX = graphicsXFor(visibleIntervalOfLine.getEnd().getRecordedInstant());
		g.fillRect(startX,threadGraphicsY,endX - startX,threadGraphicsHeight);
	}

	private LogInstant instantFor(int graphicsX) {
		return new LogInstant( uiTimeScale.viewToModel(graphicsX),0);
	}

	public void repaint(LogInterval logInterval) {
		repaint(boundsFor(logInterval, 0, threadedSystem.getThreads().size()-1));
	}
	
	private void repaint(LogInterval logInterval, int minThreadIndex, int maxThreadIndex) {
		repaint(boundsFor(logInterval, minThreadIndex, maxThreadIndex));
	}

	private Rectangle boundsFor(LogInterval logInterval, int threadSetStartIndex, int threadSetEndIndex) {
		int x=graphicsXFor(logInterval.getStart().getRecordedInstant());
		int width=graphicsXFor(logInterval.getEnd().getRecordedInstant()) - x;
		int threads = threadSetEndIndex - threadSetStartIndex +1;
		return new Rectangle(x,graphicsYFor(threadSetStartIndex),width,threads * threadGraphicsHeight);
	}

	private int graphicsYFor(int threadIndex) {
		return threadIndex * threadGraphicsHeight;
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
		ArrayList<ThreadModel> threads = new ArrayList<ThreadModel>(
				threadedSystem.getThreads());
		int threadIndex = point.y/threadGraphicsHeight;
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
