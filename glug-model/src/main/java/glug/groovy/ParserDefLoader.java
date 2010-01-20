package glug.groovy;

import glug.parser.logmessages.GroovyDrivenLogMessageParser;
import glug.parser.logmessages.JVMUptimeParser;
import glug.parser.logmessages.LogMessageParser;
import glug.parser.logmessages.LogMessageParserRegistry;
import groovy.lang.Binding;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;
import org.joda.time.Duration;

import java.util.List;
import java.util.regex.Matcher;

import static com.google.common.collect.Lists.newArrayList;

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
        binding.setProperty("durationInMillis",new HandyDurationFromTextConverterFunction());
        return binding;
    }

    public class RegistrationFunction {
        private List<LogMessageParser> parsers = newArrayList((LogMessageParser)new JVMUptimeParser());

        public void call(ParserDef... things) {
            for (ParserDef parserDef : things) {
                parsers.add(new GroovyDrivenLogMessageParser(parserDef));
            }
            System.out.println("Yep "+parsers);
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
}
