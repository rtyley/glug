package glug.gui;

import static java.lang.Math.round;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.time.LogInterval;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Map.Entry;

public class ThreadPainter {
	
	private final UILogTimeScale uiLogTimeScale;
	private final UIThreadScale uiThreadScale;

	public ThreadPainter(UILogTimeScale uiLogTimeScale, UIThreadScale uiThreadScale) {
		this.uiLogTimeScale = uiLogTimeScale;
		this.uiThreadScale = uiThreadScale;
	}
	
	public void paintThread(ThreadModel threadModel, int threadIndex, LogInterval visibleInterval, Graphics2D g) {
		int durationFor1Pixel = (int) round(uiLogTimeScale.getMillisecondsPerPixel());
		int threadGraphicsY = uiThreadScale.modelThreadIndexToView(threadIndex);
		int threadGraphicsHeight = uiThreadScale.modelThreadIndexToView(threadIndex+1) - threadGraphicsY;
		
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
						plotBlock(visibleIntervalToPlot, threadGraphicsY,threadGraphicsHeight, g); // finish with the old block
						visibleIntervalToPlot = visibleIntervalOfCurrentSigInt;  // start the new block
					}
				}
			}
			if (visibleIntervalToPlot != null) {
				plotBlock(visibleIntervalToPlot, threadGraphicsY,threadGraphicsHeight, g);
			}
			
		}
	}

	private boolean close(LogInterval visibleIntervalToPlot, int durationFor1Pixel, LogInterval visibleIntervalOfCurrentSigInt) {
		return (visibleIntervalOfCurrentSigInt.getStart().getMillis()-visibleIntervalToPlot.getEnd().getMillis())<durationFor1Pixel;
	}

	private void plotBlock(LogInterval visibleIntervalOfLine, int threadYStart, int threadGraphicsHeight, Graphics2D g) {
		int startX = uiLogTimeScale.modelToView(visibleIntervalOfLine.getStart());
		int endX = uiLogTimeScale.modelToView(visibleIntervalOfLine.getEnd());
		g.fillRect(startX, threadYStart, endX - startX,threadGraphicsHeight);
	}
}
