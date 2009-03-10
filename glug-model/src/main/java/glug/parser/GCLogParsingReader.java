package glug.parser;

import static java.lang.Math.round;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

import org.joda.time.Duration;

public class GCLogParsingReader {

	private static NumberFormat secondsNumberFormat = NumberFormat.getNumberInstance();
	
	private boolean endOfStream = false;
	private BufferedReader bufferedReader;
	private GarbageCollectionLogParser garbageCollectionLogParser;
	
	public GCLogParsingReader(BufferedReader bufferedReader, GarbageCollectionLogParser garbageCollectionLogParser) {
		this.bufferedReader = bufferedReader;
		this.garbageCollectionLogParser = garbageCollectionLogParser;
	}

	public GarbageCollection parseNext() throws IOException, ParseException {
		String line = bufferedReader.readLine();
		if (line==null) {
			return null;
		}
		
		// parse start of the first line
		String uptimeString = line.substring(0, line.indexOf(':'));
		Duration uptimeAtStartOfCollection = durationFrom(uptimeString);

		BracketMatcher brackets = new BracketMatcher();
		while (!brackets.areClosed(line)) { 
			line=bufferedReader.readLine();
			if (line==null) {
				return null;
			}
		}
		// parse the end of the last line
		int lastIndexOfCommaSpace = line.lastIndexOf(", ");
		String collectionDurationString = line.substring(lastIndexOfCommaSpace+2, line.length()-5);
		Duration collectionDuration = durationFrom(collectionDurationString);
		
		return new GarbageCollection(uptimeAtStartOfCollection, collectionDuration);
	}

	private Duration durationFrom(String secondsString) throws ParseException {
		return new Duration(round(secondsNumberFormat.parse(secondsString).doubleValue()*1000));
	}

	public boolean endOfStream() {
		return endOfStream;
	}

}
