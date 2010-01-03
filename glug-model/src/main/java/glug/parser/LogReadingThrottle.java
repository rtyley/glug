package glug.parser;

public class LogReadingThrottle {

    public boolean shouldReportGiven(int numLinesRead, int numEventsFound) {
        return false;
    }
}
