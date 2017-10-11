package glug.gui;

import glug.model.time.LogInstant;

public class UILogTimeScale {

    private final UITimeScale uiTimeScale;

    public UILogTimeScale(UITimeScale uiTimeScale) {
        this.uiTimeScale = uiTimeScale;
    }

    public int modelToView(LogInstant logInstant) {
        return uiTimeScale.modelToView(logInstant.getRecordedInstant());
    }

    public double getMillisecondsPerPixel() {
        return uiTimeScale.getMillisecondsPerPixel();
    }

}
