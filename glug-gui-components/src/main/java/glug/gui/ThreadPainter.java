package glug.gui;

import com.madgag.interval.Interval;
import com.madgag.interval.collections.IntervalMap;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;
import glug.parser.GlugConfig;

import java.awt.*;
import java.util.Collection;

import static com.madgag.interval.Bound.MAX;
import static com.madgag.interval.Bound.MIN;
import static com.madgag.interval.SimpleInterval.overlap;
import static com.madgag.interval.SimpleInterval.union;
import static java.lang.Math.round;

public class ThreadPainter {
	
	private final UILogTimeScale uiLogTimeScale;
	private final UIThreadScale uiThreadScale;
	private final GlugConfig glugConfig;

	public ThreadPainter(UILogTimeScale uiLogTimeScale, UIThreadScale uiThreadScale, GlugConfig glugConfig) {
		this.uiLogTimeScale = uiLogTimeScale;
		this.uiThreadScale = uiThreadScale;
		this.glugConfig = glugConfig;
	}
	
	public void paintThread(ThreadModel threadModel, int threadIndex, LogInterval visibleInterval, Graphics2D g) {
		int durationFor1Pixel = (int) round(uiLogTimeScale.getMillisecondsPerPixel());
		int threadGraphicsY = uiThreadScale.modelThreadIndexToView(threadIndex);
		int threadGraphicsHeight = uiThreadScale.modelThreadIndexToView(threadIndex+1) - threadGraphicsY;
		
		
		
		for (IntervalTypeDescriptor intervalTypeDescriptor : glugConfig.getIntervalTypes()) {
			plotIntervalsOfType(intervalTypeDescriptor, threadModel,
					visibleInterval, g, durationFor1Pixel, threadGraphicsY,
					threadGraphicsHeight);
		}
	}

	private void plotIntervalsOfType(
			IntervalTypeDescriptor intervalTypeDescriptor,
			ThreadModel threadModel, LogInterval visibleInterval, Graphics2D g,
			int durationFor1Pixel, int threadGraphicsY, int threadGraphicsHeight) {
		g.setColor(intervalTypeDescriptor.getColour());

		IntervalMap<LogInstant, SignificantInterval> significantIntervals = threadModel.significantIntervalsFor(intervalTypeDescriptor);
		
		if (significantIntervals==null) {
			return;
		}
		
		Collection<SignificantInterval> sigInts = significantIntervals.getEventsDuring(visibleInterval);
		
		Interval<LogInstant> visibleIntervalToPlot = null;
		
		for (SignificantInterval significantInterval : sigInts) {
			Interval<LogInstant> visibleIntervalOfCurrentSigInt =  overlap(significantInterval.getLogInterval(),visibleInterval);
			if (visibleIntervalOfCurrentSigInt!=null) {
				if (visibleIntervalToPlot==null) {
					visibleIntervalToPlot = visibleIntervalOfCurrentSigInt;
				} else if (close(visibleIntervalToPlot, durationFor1Pixel, visibleIntervalOfCurrentSigInt)) {
					visibleIntervalToPlot = union(visibleIntervalToPlot,visibleIntervalOfCurrentSigInt);
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

	private boolean close(Interval<LogInstant> visibleIntervalToPlot, int durationFor1Pixel, Interval<LogInstant> visibleIntervalOfCurrentSigInt) {
		return (visibleIntervalOfCurrentSigInt.get(MIN).getMillis()-visibleIntervalToPlot.get(MAX).getMillis())<durationFor1Pixel;
	}

	private void plotBlock(Interval<LogInstant> visibleIntervalOfLine, int threadYStart, int threadGraphicsHeight, Graphics2D g) {
		int startX = uiLogTimeScale.modelToView(visibleIntervalOfLine.get(MIN));
		int endX = uiLogTimeScale.modelToView(visibleIntervalOfLine.get(MAX));
		g.fillRect(startX, threadYStart, endX - startX,threadGraphicsHeight);
	}
}
