package glug.gui;

import glug.model.time.LogInstant;
import glug.model.time.LogInterval;
import glug.parser.LogLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.joda.time.Duration.standardSeconds;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogLoadingTaskTest {

    @Mock
    LogLoader logLoader;
    @Mock
    DataLoadedUIUpdater uiUpdater;

    @Test
    public void shouldNotifyUIOfEveryLoadReportIncludingTheEndOfStream() throws Exception {

        int numLinesLoadedBetweenUIUpdates = 2;

        LogInterval intervalUpdated = LogInterval.apply(standardSeconds(1), LogInstant.apply(2000L, 0));
        LogLoader.LoadReport endOfStreamLogReport = new LogLoader.LoadReport(true, intervalUpdated);

        when(logLoader.loadLines(numLinesLoadedBetweenUIUpdates)).thenReturn(endOfStreamLogReport);

        LogLoadingTask logLoadingTask = new LogLoadingTask(logLoader, uiUpdater, numLinesLoadedBetweenUIUpdates);
        logLoadingTask.execute();

        logLoadingTask.get();
        Thread.sleep(1000);
        verify(uiUpdater).updateUI(intervalUpdated);
    }

}
