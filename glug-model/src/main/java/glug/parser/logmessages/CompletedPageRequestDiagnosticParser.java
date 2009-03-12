package glug.parser.logmessages;

public class CompletedPageRequestDiagnosticParser extends CompletedPageRequestParser {

	@Override
	public String getLoggerClassName() {
		return "com.gu.performance.diagnostics.requestlogging.RequestLoggingFilter";
	}

}
