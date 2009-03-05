/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gu.glug.gui;

import static java.lang.Math.ceil;
import static java.lang.Math.round;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.SortedSet;

import javax.swing.JComponent;
import javax.swing.ToolTipManager;

import org.joda.time.Instant;
import org.joda.time.Interval;

import com.gu.glug.SignificantInterval;
import com.gu.glug.ThreadModel;
import com.gu.glug.ThreadedSystem;
import com.gu.glug.parser.logmessages.CompletedPageRequest;
import com.gu.glug.time.LogInstant;
import com.gu.glug.time.LogInterval;

/**
 * 
 * @author roberto
 */
public class ThreadedSystemViewComponent extends JComponent {

	private static final long serialVersionUID = 1L;
	private double millisecondsPerPixel = 0.25d;
	private ThreadedSystem threadedSystem;
	private LogInterval intervalCoveredByAllThreads;
	LogarithmicBoundedRange logarithmicBoundedRange;

	public ThreadedSystemViewComponent(ThreadedSystem threadedSystem) {
		this.threadedSystem = threadedSystem;
		cacheIntervalCoveredByAllThreads();
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
		int requiredWidth = (int) ceil(getDrawDistanceFor(intervalCoveredByAllThreads));
		return new Dimension(requiredWidth, threadedSystem.getNumThreads());
	}

	private boolean containsData() {
		return intervalCoveredByAllThreads != null;
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
		super.paintComponent(g);
		Graphics2D graphics2D = (Graphics2D) g;
		Rectangle clipBounds = graphics2D.getClipBounds();
		cacheIntervalCoveredByAllThreads();
		if (intervalCoveredByAllThreads != null) {
			Interval visibleInterval = visibleIntervalFor(clipBounds);
			int threadIndex = 0;
			for (ThreadModel threadModel : threadedSystem.getThreads()) {
				for (SignificantInterval significantInterval : threadModel
						.getSignificantIntervalsFor(visibleInterval)) {
					LogInterval aa = significantInterval.getLogInterval();
					if (significantInterval.getType() instanceof CompletedPageRequest) {
						g.setColor(Color.RED);
					} else {
						g.setColor(Color.BLACK);
					}
					g.drawLine(graphicsXFor(aa.getStart().getInstant()),
							-threadIndex,
							graphicsXFor(aa.getEnd().getInstant()),
							-threadIndex);
				}
				--threadIndex;
			}
		}

	}

	void setMillisecondsPerPixel(double millisecondsPerPixel) {
		this.millisecondsPerPixel = millisecondsPerPixel;
		setSize(getPreferredSize());
		// System.out.println("millisecondsPerPixel = "+millisecondsPerPixel);
		this.repaint();
	}

	private int graphicsXFor(Instant instant) {
		return (int) round((differenceInMillisFromStartOfIntervalCoveredByAllThreadsFor(instant)) / millisecondsPerPixel);
	}

	private long differenceInMillisFromStartOfIntervalCoveredByAllThreadsFor(Instant instant) {
		return instant.getMillis() - intervalCoveredByAllThreads.getStart().getMillis();
	}

	private Interval visibleIntervalFor(Rectangle clipBounds) {
		return new Interval(instantFor(clipBounds.getMinX()),
				instantFor(clipBounds.getMaxX()));
	}

	private double getDrawDistanceFor(LogInterval interval) {
		return interval.toDurationMillis() / millisecondsPerPixel;
	}

	private Instant instantFor(double graphicsX) {
		return intervalCoveredByAllThreads.getStart().getInstant().plus(
				round(graphicsX * millisecondsPerPixel));
	}

	public void repaint(Interval interval) {
		cacheIntervalCoveredByAllThreads();
		repaint(graphicsXFor(interval.getStart().toInstant()) - 1, 0,
				graphicsXFor(interval.getEnd().toInstant()) + 1, threadedSystem
						.getNumThreads());
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
		Instant instant = instantFor(event.getX());
		SortedSet<SignificantInterval> significantIntervalsFor = thread
				.getSignificantIntervalsFor(new LogInstant(instant, 0));
		if (significantIntervalsFor.isEmpty()) {
			return null;
		}
		return "<html>" + significantIntervalsFor.toString() + "</html>";
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
	protected void processComponentEvent(ComponentEvent e) {
		super.processComponentEvent(e);
		if (e.getID() == ComponentEvent.COMPONENT_RESIZED) {
			
		}
	}



}
