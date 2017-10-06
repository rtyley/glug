package glug.parser;

import glug.FooLogLineParser;
import glug.model.SignificantInterval;

import java.io.IOException;
import java.io.LineNumberReader;
import java.text.ParseException;


public class LogParsingReader {

    private final LineNumberReader lineNumberReader;
    private final FooLogLineParser logLineParser;
    private boolean endOfStream = false;

    public LogParsingReader(LineNumberReader lineNumberReader, FooLogLineParser logLineParser) {
        this.lineNumberReader = lineNumberReader;
        this.logLineParser = logLineParser;
    }

    public SignificantInterval parseNext() throws IOException, ParseException {
        String line = lineNumberReader.readLine();
        if (line == null) {
            endOfStream = true;
            return null;
        }
        return logLineParser.parse(line, lineNumberReader.getLineNumber());
    }

    public boolean endOfStream() {
        return endOfStream;
    }

}
