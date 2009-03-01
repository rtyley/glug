package com.gu.glug.gui;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.joda.time.Interval;
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


public class LogLoaderTest {
	@Test
	public void shouldLoadExampleFile() throws Exception {
		File file = new File("/home/roberto/guardian/incident.20090225/r2frontend.respub01.log.gz");
		BufferedReader reader = new BufferedReader(new InputStreamReader( new GZIPInputStream(new FileInputStream(file))));
		
		ThreadedSystem threadedSystem = new ThreadedSystem();
		LogLoader logLoader=new LogLoader(new LogParsingReader(reader,new LogLineParser(new LogCoordinateParser(threadedSystem),LogMessageParserRegistry.EXAMPLE)));
		while (!logLoader.loadLines(100000).endOfStreamReached()) {
			System.out.println(threadedSystem.getIntervalCoveredByAllThreads());
		}
	}
	
	@Test
	public void shouldTrackTotalIntervalUpdated() throws Exception {
		LogParsingReader reader = mock(LogParsingReader.class);
		ThreadedSystem threadedSystem = new ThreadedSystem();
		ThreadModel thread = threadedSystem.getOrCreateThread("blahThread");
		
		SignificantIntervalOccupier significantIntervalOccupierStub = mock(SignificantIntervalOccupier.class);
		when(reader.parseNext()).thenReturn(
				new SignificantInterval(thread, significantIntervalOccupierStub, new Interval(0,1000)),
				new SignificantInterval(thread, significantIntervalOccupierStub, new Interval(3000,4000)));
		LogLoader logLoader=new LogLoader(reader);
		LoadReport loadReport = logLoader.loadLines(2);

		assertThat(loadReport.getUpdatedInterval(), equalTo(new Interval(0,4000)));
	}
}
