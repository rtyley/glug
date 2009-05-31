package glug.parser;

import static glug.parser.logmessages.CompletedPageRequestParser.PAGE_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import glug.model.SignificantInterval;
import glug.model.SignificantIntervalOccupier;
import glug.model.ThreadModel;
import glug.model.ThreadedSystem;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;
import glug.parser.LogLoader.LoadReport;
import glug.parser.logmessages.LogMessageParserRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.zip.GZIPInputStream;

import org.junit.Test;

public class LogLoaderTest {

	@Test
	public void shouldTrackTotalIntervalUpdated() throws Exception {
		LogParsingReader reader = mock(LogParsingReader.class);
		ThreadedSystem threadedSystem = new ThreadedSystem();
		ThreadModel thread = threadedSystem.getOrCreateThread("blahThread");
		
		SignificantIntervalOccupier significantIntervalOccupierStub = PAGE_REQUEST.with("blah");
		when(reader.parseNext()).thenReturn(
				new SignificantInterval(significantIntervalOccupierStub, new LogInterval(new LogInstant(1000,1),new LogInstant(2000,2))),
				new SignificantInterval(significantIntervalOccupierStub, new LogInterval(new LogInstant(3000,3),new LogInstant(4000,4))));
		LogLoader logLoader=new LogLoader(reader);
		LoadReport loadReport = logLoader.loadLines(2);

		assertThat(loadReport.getUpdatedInterval(), equalTo(new LogInterval(new LogInstant(1000,1),new LogInstant(4000,4))));
	}
	
	public void shouldLoadExampleFile() throws Exception {
		File file = new File("/home/roberto/development/glug-sample-logs/gtlogglugger/lunchtime.log.gz");
		LineNumberReader reader = new LineNumberReader(new InputStreamReader( new GZIPInputStream(new FileInputStream(file))));
		
		ThreadedSystem threadedSystem = new ThreadedSystem();
		LogLoader logLoader=new LogLoader(new LogParsingReader(reader,new LogLineParser(new LogCoordinateParser(threadedSystem),LogMessageParserRegistry.EXAMPLE)));
		LoadReport lr;
		do {
			lr=logLoader.loadLines(100000);
			System.out.println(lr);
		} while (!lr.endOfStreamReached());
		System.out.println(threadedSystem.getIntervalCoveredByAllThreads());
	}
	

}
