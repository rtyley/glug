package glug.gui;

import glug.gui.zoom.ZoomFactorSlideUpdater;
import glug.model.ThreadedSystem;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataLoadedUIUpdaterTest {
	@Mock ThreadedSystem threadedSystem;
	@Mock UITimeScale uiTimeScale;
	@Mock ZoomFactorSlideUpdater zoomFactorSlideUpdater;
	@Mock UIThreadScale threadScale;

    @Test
    public void shouldSetUITimeScaleFullIntervalToFullIntervalSpannedByAllThreads() throws Exception {
        LogInterval totalSpanningLogInterval = new LogInterval(new LogInstant(1000, 1), new LogInstant(5000, 5));
        when(threadedSystem.getIntervalCoveredByAllThreads()).thenReturn(totalSpanningLogInterval);

        DataLoadedUIUpdater dataLoadedUIUpdater = new DataLoadedUIUpdater(threadedSystem,uiTimeScale,threadScale,zoomFactorSlideUpdater);
        LogInterval smallIntervalThatHasJustBeenUpdated = new LogInterval(new LogInstant(3000, 3), new LogInstant(4000, 4));
        
        dataLoadedUIUpdater.updateUI(smallIntervalThatHasJustBeenUpdated);
        
        verify(uiTimeScale).setFullInterval(totalSpanningLogInterval.toJodaInterval());

    }
}
