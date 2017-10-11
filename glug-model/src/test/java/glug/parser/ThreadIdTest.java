package glug.parser;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class ThreadIdTest {

    @SuppressWarnings("unchecked")
    @Test
    public void shouldChopItAllUp() {
        assertThat(new ThreadId("resin-tcp-connection-respub.gutest.gnl:6802-23").getParts(),
                equalTo(asList((Comparable<?>) "resin-tcp-connection-respub.gutest.gnl:", 6802, "-", 23)));
    }

}
