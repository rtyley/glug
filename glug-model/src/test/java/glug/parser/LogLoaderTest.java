package glug.parser;

import com.madgag.interval.Interval;
import glug.groovy.ParserDefLoader;
import glug.model.SignificantInterval;
import glug.model.SignificantIntervalOccupier;
import glug.model.ThreadModel;
import glug.model.ThreadedSystem;
import glug.model.time.LogInstant;
import glug.model.time.LogInterval;
import glug.parser.LogLoader.LoadReport;
import glug.parser.logmessages.LogMessageParserRegistry;
import groovy.lang.GroovyCodeSource;
import org.junit.Test;

import java.io.File;

import static com.google.common.collect.ImmutableMap.of;
import static com.madgag.interval.SimpleInterval.interval;
import static glug.parser.logmessages.CompletedPageRequestParser.PAGE_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LogLoaderTest {

    @Test
    public void shouldTrackTotalIntervalUpdated() throws Exception {
        LogParsingReader reader = mock(LogParsingReader.class);
        ThreadedSystem threadedSystem = new ThreadedSystem();
        ThreadModel thread = threadedSystem.getOrCreateThread("blahThread");

        SignificantIntervalOccupier significantIntervalOccupierStub = PAGE_REQUEST.with("blah");
        when(reader.parseNext()).thenReturn(
                new SignificantInterval(of("type", "My Type"), new LogInterval(new LogInstant(1000, 1), new LogInstant(2000, 2))),
                new SignificantInterval(of("type", "My Type"), new LogInterval(new LogInstant(3000, 3), new LogInstant(4000, 4))));
        LogLoader logLoader = new LogLoader(reader);
        LoadReport loadReport = logLoader.loadLines(2);

        assertThat(loadReport.getUpdatedInterval(), equalTo((Interval<LogInstant>) interval(new LogInstant(1000, 1), new LogInstant(4000, 4))));
    }

    //@Test
    public void shouldLoadExampleFile() throws Exception {
        File file = new File("/home/roberto/development/glug-logs/respubs/r2frontend.log.2009-12-18.respub01.gz");

        ThreadedSystem threadedSystem = new ThreadedSystem();

        LogLoaderFactory logLoaderFactory = new LogLoaderFactory();
        LogMessageParserRegistry registry = new ParserDefLoader().load(new GroovyCodeSource(new File("/home/roberto/development/glug/glug-model/src/main/java/glug/parser/logmessages/DefaultParsers.groovy")));
        LogLoader logLoader = logLoaderFactory.createLoaderFor(file, threadedSystem, registry);
        LoadReport lr;
        do {
            lr = logLoader.loadLines(100000);
            System.out.println(lr);
        } while (!lr.endOfStreamReached());
        System.out.println(threadedSystem.getIntervalCoveredByAllThreads());
    }


}
