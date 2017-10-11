package glug.gui;

import java.awt.*;

import static java.lang.Math.floor;
import static java.lang.Math.round;

public class UIThreadScale {

    private int numThreads;

    private double pixelsPerThread = 4;

    public void setNumThreads(int numThreads) {

    }

    public int fullModelToViewLength() {
        return (int) round(numThreads * pixelsPerThread);
    }

    public double getPixelsPerThread() {
        return pixelsPerThread;
    }

    public int modelThreadIndexToView(int threadIndex) {
        return (int) round(threadIndex * pixelsPerThread);
    }

    public int viewToModelThreadIndex(Point point) {
        return viewToModelThreadIndex(point.y);
    }

    public int viewToModelThreadIndex(int y) {
        return (int) floor(y / pixelsPerThread);
    }
}
