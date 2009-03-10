package glug.gui;

import static java.awt.Color.RED;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.joda.time.Duration.standardSeconds;
import static org.junit.Assert.assertThat;
import glug.model.SignificantInterval;
import glug.model.ThreadedSystem;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;

import org.junit.Before;
import org.junit.Test;


public class SwingHtmlStyleThreadReporterTest {
	
	private SwingHtmlStyleThreadReporter reporter;

	@Before
	public void setUp() {
		reporter = new SwingHtmlStyleThreadReporter();		
	}

	@Test
	public void shouldIncludeUptimeIfKnown() {
		ThreadedSystem threadedSystem = new ThreadedSystem();
		threadedSystem.getUptime().addUptime(new SignificantInterval(null,null,new LogInterval(standardSeconds(3),new LogInstant(3000))));
		assertThat(reporter.uptimeStringFor(threadedSystem, new LogInstant(1000)),containsString("uptime: 1000 ms"));
	}
	
	@Test
	public void shouldNotThrowExceptionIfUptimeIsNotKnown() {
		assertThat(reporter.uptimeStringFor(new ThreadedSystem(), new LogInstant(1000)),equalTo(""));
	}
	
	@Test
	public void shouldReturnRightHexForColour() throws Exception {
		assertThat(reporter.hexFor(RED),equalToIgnoringCase("FF0000"));
	}
}
