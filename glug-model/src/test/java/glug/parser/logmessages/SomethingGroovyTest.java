package glug.parser.logmessages;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.junit.Test;

import java.io.File;

public class SomethingGroovyTest {

    @Test
    public void justSomeCrazyFacts() throws Exception {
        Binding binding = new Binding();
        binding.setProperty("registerParser",new Zork());
        GroovyShell shell = new GroovyShell(binding);
        shell.evaluate(new File("/home/roberto/development/glug/glug-model/src/main/java/glug/parser/logmessages/Chunk.groovy"));
        
    }

    public class Zork {
        public void call(SomethingGroovy somethingGroovy) {
            System.out.println("Yep "+somethingGroovy.pattern);
        }
    }
}
