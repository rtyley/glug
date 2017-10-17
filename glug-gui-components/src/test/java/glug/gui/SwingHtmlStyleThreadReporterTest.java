package glug.gui;

import glug.model.ThreadModel;
import glug.model.ThreadedSystem;
import glug.model.time.LogInterval;
import org.junit.Before;
import org.junit.Test;

import static java.awt.Color.RED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.joda.time.Duration.standardSeconds;


public class SwingHtmlStyleThreadReporterTest {

    private SwingHtmlStyleThreadReporter reporter;
    private ThreadedSystem threadedSystem;

    @Before
    public void setUp() {
        reporter = new SwingHtmlStyleThreadReporter();
        threadedSystem = new ThreadedSystem();
    }

    @Test
    public void shouldCopeWithAbsentIntervalTypesForRequestedInstant() {
        ThreadModel thread = threadedSystem.getOrCreateThread("my-thread");
        reporter.htmlSyledReportFor(thread, LogInstant.apply(1000));
    }


    @Test
    public void shouldIncludeUptimeIfKnown() {
        threadedSystem.uptime().addUptime(new LogInterval(standardSeconds(3), LogInstant.apply(3000)));
        assertThat(reporter.uptimeStringFor(threadedSystem, LogInstant.apply(1000)), containsString("uptime: 1.000 s"));
    }

    @Test
    public void shouldNotThrowExceptionIfUptimeIsNotKnown() {
        assertThat(reporter.uptimeStringFor(threadedSystem, LogInstant.apply(1000)), equalTo(""));
    }

    @Test
    public void shouldReturnRightHexForColour() {
        assertThat(reporter.hexFor(RED), equalToIgnoringCase("FF0000"));
    }
}
