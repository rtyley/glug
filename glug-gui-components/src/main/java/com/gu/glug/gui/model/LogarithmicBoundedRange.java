package com.gu.glug.gui.model;

import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.round;

import javax.swing.BoundedRangeModel;

public class LogarithmicBoundedRange {

	private final double multFactor = 1<<16;
	private final BoundedRangeModel linearBoundedRangeModel;

	public LogarithmicBoundedRange(BoundedRangeModel linearBoundedRangeModel) {
		this.linearBoundedRangeModel = linearBoundedRangeModel;
	}

	public void setMaxMillisecondsPerPixel(double millisecondsPerPixel) {
		linearBoundedRangeModel.setMaximum(linearScaleValueFor(millisecondsPerPixel));
	}
	
	public void setMinMillisecondsPerPixel(double millisecondsPerPixel) {
		linearBoundedRangeModel.setMinimum(linearScaleValueFor(millisecondsPerPixel));
	}
	
	public void setCurrentMillisecondsPerPixel(double millisecondsPerPixel) {
		linearBoundedRangeModel.setValue(linearScaleValueFor(millisecondsPerPixel));
	}
	
	public double getCurrentMillisecondsPerPixel() {
		return millisecondsPerPixelFor(linearBoundedRangeModel.getValue());
	}
	
	private int linearScaleValueFor(double millisecondsPerPixel) {
		return (int) round(log(millisecondsPerPixel) * multFactor);
	}
	
	private double millisecondsPerPixelFor(int linearScaleValue) {
		return exp((linearScaleValue / multFactor));
	}

}
