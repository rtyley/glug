package glug.gui;

import static java.awt.Color.RED;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class SwingHtmlStyleThreadReporterTest {
	@Test
	public void shouldReturnRightHexForColour() throws Exception {
		SwingHtmlStyleThreadReporter reporter = new SwingHtmlStyleThreadReporter();
		assertThat(reporter.hexFor(RED),equalToIgnoringCase("FF0000"));
	}
}
