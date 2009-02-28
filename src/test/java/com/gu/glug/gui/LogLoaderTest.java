package com.gu.glug.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.junit.Test;

import com.gu.glug.ThreadedSystem;


public class LogLoaderTest {
	@Test
	public void shouldLoadExampleFile() throws Exception {
		File file = new File("/home/roberto/development/glug/src/test/resources/r2frontend.respub01.first10000.log.gz");
		BufferedReader reader = new BufferedReader(new InputStreamReader( new GZIPInputStream(new FileInputStream(file))));
		
		LogLoader logLoader=new LogLoader(reader,new ThreadedSystem());
		logLoader.loadLines(10000);
	}
}
