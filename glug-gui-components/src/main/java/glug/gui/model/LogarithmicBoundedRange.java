package glug.gui.model;

import javax.swing.*;

import static java.lang.Math.*;

public class LogarithmicBoundedRange {

    private final double multFactor = 1 << 16;
    private final BoundedRangeModel linearBoundedRangeModel;

    public LogarithmicBoundedRange(BoundedRangeModel linearBoundedRangeModel) {
        this.linearBoundedRangeModel = linearBoundedRangeModel;
    }

    public void setMaxMillisecondsPerPixel(double millisecondsPerPixel) {
        linearBoundedRangeModel.setMaximum((int) ceil(linearScaleDoubleValueFor(millisecondsPerPixel)));
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
        return (int) round(linearScaleDoubleValueFor(millisecondsPerPixel));
    }

    private double linearScaleDoubleValueFor(double millisecondsPerPixel) {
        return round(log(millisecondsPerPixel) * multFactor);
    }

    private double millisecondsPerPixelFor(int linearScaleValue) {
        return exp((linearScaleValue / multFactor));
    }

}
