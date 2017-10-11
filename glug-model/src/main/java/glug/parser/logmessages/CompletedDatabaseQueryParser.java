package glug.parser.logmessages;

import glug.model.IntervalTypeDescriptor;

import java.util.regex.Pattern;

import static java.awt.Color.BLACK;

/*
 2009-02-25 00:00:00,093 [resin-tcp-connection-respub.gul3.gnl:6802-39] INFO  com.gu.r2.common.diagnostic.database.PreparedStatementProxy - Query "load com.gu.r2.common.model.page.LivePage" (component: slotMachineWithConstantHeading) completed in 20 ms

 Query "load com.gu.r2.common.model.page.LivePage" (component: slotMachineWithConstantHeading) completed in 20 ms
 */
public class CompletedDatabaseQueryParser {

    public static final IntervalTypeDescriptor DATABASE_QUERY = new IntervalTypeDescriptor(BLACK, "Database Query");

    private static final Pattern databaseQueryPattern = Pattern.compile("Query \"(.+?)\" \\(component: (.+?)\\) completed in (\\d+?) ms");

    public CompletedDatabaseQueryParser() {
        //super("com.gu.r2.common.diagnostic.database.PreparedStatementProxy", databaseQueryPattern);
    }

}