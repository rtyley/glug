package com.gu.glug.gui;

import static org.mockito.Mockito.mock;

import java.io.BufferedReader;

import org.junit.Test;

import com.gu.glug.SignificantInterval;
import com.gu.glug.parser.LogLineParser;


public class LogParsingReaderTest {
	@Test
	public void shouldReadStuff() throws Exception {
		BufferedReader bufferedReader = mock(BufferedReader.class);
		LogLineParser logLineParser = mock(LogLineParser.class);
		
		LogParsingReader logParsingReader = new LogParsingReader(bufferedReader, logLineParser);
		
		SignificantInterval significantInterval = logParsingReader.parseNext();
		
	}
}
