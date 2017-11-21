package glug.parser;

import com.madgag.interval.Bound;
import com.madgag.interval.Interval;
import glug.FooLogLineParser;
import glug.model.SignificantInterval;
import glug.model.time.LogInstant;
import org.joda.time.Duration;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.text.ParseException;

import static com.madgag.interval.Bound.MAX;
import static com.madgag.interval.Bound.MIN;


public class LogParsingReader {

    private final LineNumberReader lineNumberReader;
    private final Writer writer;
    private final FooLogLineParser logLineParser;
    private boolean endOfStream = false;

    public LogParsingReader(LineNumberReader lineNumberReader, Writer writer, FooLogLineParser logLineParser) {
        this.lineNumberReader = lineNumberReader;
        this.writer = writer;
        this.logLineParser = logLineParser;
    }

    public SignificantInterval parseNext() throws IOException, ParseException {
        String line = lineNumberReader.readLine();
        if (line == null) {
            endOfStream = true;
            return null;
        }
        SignificantInterval significantInterval = logLineParser.parse(line, lineNumberReader.getLineNumber());
        Interval<LogInstant> logInterval = significantInterval.getLogInterval();

        LogInstant start = logInterval.get(MIN);
        LogInstant end = logInterval.get(MAX);
        Duration d = new Duration(start.getMillis(), end.getMillis());
        writer.write(start +" "+d.getMillis()+" ms "+significantInterval);
        return significantInterval;
    }

    public boolean endOfStream() {
        return endOfStream;
    }

}
