package glug.groovy;

import glug.groovy.ParserDef;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.joda.time.Duration;
import org.junit.Test;

import java.io.File;
import java.util.regex.Matcher;

public class ParserDefTest {

    @Test
    public void justSomeCrazyFacts() throws Exception {
        Binding binding = new Binding();
        binding.setProperty("register",new Zork());
        binding.setProperty("durationInMillis",new Zelda());
        GroovyShell shell = new GroovyShell(binding);
        shell.evaluate(new File("/home/roberto/development/glug/glug-model/src/main/java/glug/parser/logmessages/Chunk.groovy"));
        
    }

    public class Zork {
        public void call(ParserDef... things) {
            String text="STREAM_RENDERED_COMMENTS 337109213 Pagination{numberPerPage=50, pageNumber=1} completed in 67 ms";
            for (ParserDef parserDef : things) {
                System.out.println("pat "+ parserDef.pattern);
                Matcher matcher = parserDef.pattern.matcher(text);
                if (matcher.matches()) {
                    Duration dur = (Duration) parserDef.duration.call(matcher);
                    System.out.println("dur "+dur);
                }
            }
            System.out.println("Yep "+things);
        }
    }

    public class Zelda {
        public Duration call(String text) {
            return new Duration(Integer.parseInt(text));
        }
    }
}