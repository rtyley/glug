package glug.parser.logmessages;

public class CompletedOpenApiEndecaRequestParser extends CompletedEndecaRequestParser {

	@Override
	public String getLoggerClassName() {
		return "com.gu.endeca.bridge.UrlENEQueryDAO";
	}

}
