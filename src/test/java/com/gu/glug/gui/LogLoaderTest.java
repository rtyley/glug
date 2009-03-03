package com.gu.glug.gui;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.zip.GZIPInputStream;

import org.junit.Test;

import com.gu.glug.SignificantInterval;
import com.gu.glug.SignificantIntervalOccupier;
import com.gu.glug.ThreadModel;
import com.gu.glug.ThreadedSystem;
import com.gu.glug.gui.LogLoader.LoadReport;
import com.gu.glug.parser.LogCoordinateParser;
import com.gu.glug.parser.LogLineParser;
import com.gu.glug.parser.LogParsingReader;
import com.gu.glug.parser.logmessages.LogMessageParserRegistry;
import com.gu.glug.time.LogInstant;
import com.gu.glug.time.LogInterval;


public class LogLoaderTest {

	@Test
	public void shouldTrackTotalIntervalUpdated() throws Exception {
		LogParsingReader reader = mock(LogParsingReader.class);
		ThreadedSystem threadedSystem = new ThreadedSystem();
		ThreadModel thread = threadedSystem.getOrCreateThread("blahThread");
		
		SignificantIntervalOccupier significantIntervalOccupierStub = mock(SignificantIntervalOccupier.class);
		when(reader.parseNext()).thenReturn(
				new SignificantInterval(thread, significantIntervalOccupierStub, new LogInterval(new LogInstant(1000,1),new LogInstant(2000,2))),
				new SignificantInterval(thread, significantIntervalOccupierStub, new LogInterval(new LogInstant(3000,3),new LogInstant(4000,4))));
		LogLoader logLoader=new LogLoader(reader);
		LoadReport loadReport = logLoader.loadLines(2);

		assertThat(loadReport.getUpdatedInterval(), equalTo(new LogInterval(new LogInstant(1000,1),new LogInstant(4000,4))));
	}
	
	@Test
	public void shouldLoadExampleFile() throws Exception {
		File file = new File("/home/roberto/guardian/incident.20090225/r2frontend.respub01.log.gz");
		LineNumberReader reader = new LineNumberReader(new InputStreamReader( new GZIPInputStream(new FileInputStream(file))));
		
		ThreadedSystem threadedSystem = new ThreadedSystem();
		LogLoader logLoader=new LogLoader(new LogParsingReader(reader,new LogLineParser(new LogCoordinateParser(threadedSystem),LogMessageParserRegistry.EXAMPLE)));
		while (!logLoader.loadLines(100000).endOfStreamReached()) {
			LogInterval intervalCoveredByAllThreads = threadedSystem.getIntervalCoveredByAllThreads();
			System.out.println(intervalCoveredByAllThreads);
		}
	}
	

}
