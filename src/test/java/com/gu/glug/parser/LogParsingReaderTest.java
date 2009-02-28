package com.gu.glug.parser;

import static org.mockito.Mockito.mock;

import java.io.BufferedReader;

import org.junit.Test;

import com.gu.glug.SignificantInterval;


public class LogParsingReaderTest {
	@Test
	public void shouldReadStuff() throws Exception {
		BufferedReader bufferedReader = mock(BufferedReader.class);
		LogLineParser logLineParser = mock(LogLineParser.class);
		
		LogParsingReader logParsingReader = new LogParsingReader(bufferedReader, logLineParser);
		
		SignificantInterval significantInterval = logParsingReader.parseNext();
		
	}
}
