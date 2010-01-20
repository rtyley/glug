package glug.parser.logmessages;

import static java.awt.Color.GREEN;
import static java.lang.Integer.parseInt;
import glug.model.IntervalTypeDescriptor;
import glug.model.SignificantIntervalOccupier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;

/*
2009-01-29 14:17:18,030 [resin-tcp-connection-respub.gutest.gnl:6802-97] INFO  com.gu.r2.common.util.http.HttpConnection - Http request for ESA http://gnm.esagroup.co.uk/football/match/1205261 completed in 50 ms

Http request for ESA http://gnm.esagroup.co.uk/football/match/1205261 completed in 50 ms
Http request for DAYLIFE http://daypi.daylife.com/xmlrest/publicapi/4.2/topic_getRelatedArticles completed in 178 ms
Http request for PLUCK http://sitelife.gutest.gnl/ver1.0/Direct/Process completed in 242 ms
Http request for ENDECA_SEARCH_GUI http://browse.gutest.co.uk/education/component completed in 1343 ms
Http request for REUTERS http://ris.rois.com/TPRjIia1148tDU*g4QbwCAfnc3bxoT7RDGD-*62*GNEzs/CTIB/POWERSEARCH3XML?CMD=NameGrouped&SearchName=banco+santader&Precision=including&StartRow=1&FORMAT=XML completed in 5352 ms
 */

public class CompletedHTTPRequestParser {
	public static final IntervalTypeDescriptor HTTP_REQUEST = new IntervalTypeDescriptor(GREEN.darker(), "HTTP Request");
	
	private static final Pattern httpRequestPattern = Pattern.compile("Http request for ([^ ]+) ([^ ]+) completed in (\\d+) ms");

	public CompletedHTTPRequestParser() {
		//super("com.gu.r2.common.util.http.HttpConnection", httpRequestPattern);
	}

	SignificantIntervalOccupier intervalOccupierFor(Matcher matcher) {
		String dbQuery = matcher.group(2);
		return HTTP_REQUEST.with(dbQuery);
	}

	Duration durationFrom(Matcher matcher) {
		String durationInMillisText = matcher.group(3);
		return new Duration(parseInt(durationInMillisText));
	}
}