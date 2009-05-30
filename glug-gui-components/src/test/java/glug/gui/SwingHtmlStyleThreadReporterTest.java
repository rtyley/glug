package glug.gui;

import static glug.parser.logmessages.CompletedPageRequestParser.PAGE_REQUEST;
import static java.awt.Color.RED;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.joda.time.Duration.standardSeconds;
import static org.junit.Assert.assertThat;
import glug.model.SignificantInterval;
import glug.model.ThreadModel;
import glug.model.ThreadedSystem;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;
import glug.parser.GlugConfig;

import org.junit.Before;
import org.junit.Test;


public class SwingHtmlStyleThreadReporterTest {
	
	private SwingHtmlStyleThreadReporter reporter;
	private ThreadedSystem threadedSystem;

	@Before
	public void setUp() {
		GlugConfig glugConfig = new GlugConfig();
		glugConfig.getIntervalTypes().add(PAGE_REQUEST);
		reporter = new SwingHtmlStyleThreadReporter(glugConfig);		
		threadedSystem = new ThreadedSystem();
	}

	@Test
	public void shouldCopeWithAbsentntervalTypesForRequestedInstant() {
		ThreadModel thread = threadedSystem.getOrCreateThread("my-thread");
		reporter.htmlSyledReportFor(thread, new LogInstant(1000));
	}
	
	
	@Test
	public void shouldIncludeUptimeIfKnown() {
		threadedSystem.uptime().addUptime(new SignificantInterval(null,null,new LogInterval(standardSeconds(3),new LogInstant(3000))));
		assertThat(reporter.uptimeStringFor(threadedSystem, new LogInstant(1000)),containsString("uptime: 1.000 s"));
	}
	
	@Test
	public void shouldNotThrowExceptionIfUptimeIsNotKnown() {
		assertThat(reporter.uptimeStringFor(threadedSystem, new LogInstant(1000)),equalTo(""));
	}
	
	@Test
	public void shouldReturnRightHexForColour() {
		assertThat(reporter.hexFor(RED),equalToIgnoringCase("FF0000"));
	}
}
