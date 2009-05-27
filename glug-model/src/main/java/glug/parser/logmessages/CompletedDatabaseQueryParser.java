package glug.parser.logmessages;

import static java.awt.Color.BLACK;
import static java.lang.Integer.parseInt;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantIntervalOccupier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;

/*
 2009-02-25 00:00:00,093 [resin-tcp-connection-respub.gul3.gnl:6802-39] INFO  com.gu.r2.common.diagnostic.database.PreparedStatementProxy - Query "load com.gu.r2.common.model.page.LivePage" (component: slotMachineWithConstantHeading) completed in 20 ms

 Query "load com.gu.r2.common.model.page.LivePage" (component: slotMachineWithConstantHeading) completed in 20 ms
 */
public class CompletedDatabaseQueryParser extends IntervalLogMessageParser {

	public static final IntervalTypeDescriptor DATABASE_QUERY = new IntervalTypeDescriptor(2, BLACK, "Database Query");
	
	private static final Pattern databaseQueryPattern = Pattern.compile("Query \"(.+?)\" \\(component: (.+?)\\) completed in (\\d+?) ms");

	public CompletedDatabaseQueryParser() {
		super("com.gu.r2.common.diagnostic.database.PreparedStatementProxy", databaseQueryPattern);
	}

	@Override
	SignificantIntervalOccupier intervalOccupierFor(Matcher matcher) {
		String dbQuery = matcher.group(1);
		return DATABASE_QUERY.with(dbQuery);
	}

	Duration durationFrom(Matcher matcher) {
		String durationInMillisText = matcher.group(3);
		return new Duration(parseInt(durationInMillisText));
	}

}