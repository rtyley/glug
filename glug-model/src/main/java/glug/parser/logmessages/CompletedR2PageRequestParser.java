package glug.parser.logmessages;



public class CompletedR2PageRequestParser extends CompletedPageRequestParser {

	@Override
	public String getLoggerClassName() {
		return "com.gu.r2.common.webutil.RequestLoggingFilter";
	}

}
