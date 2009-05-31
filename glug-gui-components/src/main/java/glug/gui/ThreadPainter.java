package glug.gui;

import static java.lang.Math.round;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantInstants;
import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.time.LogInterval;
import glug.parser.GlugConfig;

import java.awt.Graphics2D;
import java.util.Collection;

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

		SignificantInstants significantIntervals = threadModel.significantIntervalsFor(intervalTypeDescriptor);
		
		if (significantIntervals==null) {
			return;
		}
		
		Collection<SignificantInterval> sigInts = significantIntervals.getSignificantIntervalsDuring(visibleInterval);
		
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

	private boolean close(LogInterval visibleIntervalToPlot, int durationFor1Pixel, LogInterval visibleIntervalOfCurrentSigInt) {
		return (visibleIntervalOfCurrentSigInt.getStart().getMillis()-visibleIntervalToPlot.getEnd().getMillis())<durationFor1Pixel;
	}

	private void plotBlock(LogInterval visibleIntervalOfLine, int threadYStart, int threadGraphicsHeight, Graphics2D g) {
		int startX = uiLogTimeScale.modelToView(visibleIntervalOfLine.getStart());
		int endX = uiLogTimeScale.modelToView(visibleIntervalOfLine.getEnd());
		g.fillRect(startX, threadYStart, endX - startX,threadGraphicsHeight);
	}
}
