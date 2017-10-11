package glug.groovy;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import glug.parser.logmessages.GroovyDrivenLogMessageParser;
import glug.parser.logmessages.JVMUptimeParser;
import glug.parser.logmessages.LogMessageParser;
import glug.parser.logmessages.LogMessageParserRegistry;
import groovy.lang.Binding;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;
import org.joda.time.Duration;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.net.URLDecoder.decode;

public class ParserDefLoader {

    public LogMessageParserRegistry load(GroovyCodeSource groovyCodeSource) {
        RegistrationFunction registration = new RegistrationFunction();
        GroovyShell shell = new GroovyShell(standardBinding(registration));
        shell.evaluate(groovyCodeSource);
        return registration.getLogMessageParserRegistry();
    }

    private Binding standardBinding(RegistrationFunction registration) {
        Binding binding = new Binding();
        binding.setProperty("register", registration);
        binding.setProperty("durationInMillis", new HandyDurationFromTextConverterFunction());
        binding.setProperty("urlParams", new UrlParamsFunction());
        return binding;
    }

    public class RegistrationFunction {
        private List<LogMessageParser> parsers = newArrayList((LogMessageParser) new JVMUptimeParser());

        public void call(ParserDef... things) {
            for (ParserDef parserDef : things) {
                parsers.add(new GroovyDrivenLogMessageParser(parserDef));
            }
            System.out.println("Yep " + parsers);
        }

        public LogMessageParserRegistry getLogMessageParserRegistry() {
            return new LogMessageParserRegistry(parsers);
        }
    }

    public class HandyDurationFromTextConverterFunction {
        public Duration call(String text) {
            return new Duration(Integer.parseInt(text));
        }
    }


    public class UrlParamsFunction {
        public ListMultimap<String, String> call(String uriString) throws URISyntaxException, UnsupportedEncodingException {
            URI uri = new URI(uriString);
            String queryString = uri.getRawQuery();
            ListMultimap<String, String> params = LinkedListMultimap.create();
            if (queryString != null) {
                for (String keyValue : queryString.split("&")) {
                    String[] items = keyValue.split("=");
                    if (items.length == 2) {
                        params.put(decode(items[0], "UTF-8"), decode(items[1], "UTF-8"));
                    }
                }
            }
            return params;
        }
    }
}
