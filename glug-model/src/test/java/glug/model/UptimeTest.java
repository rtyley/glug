package glug.model;

import glug.model.time.LogInstant;
import glug.model.time.LogInterval;
import org.junit.Test;

import java.time.Instant;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;


public class UptimeTest {
    @Test
    public void shouldReturnCorrectUptimeForInstant() throws Exception {
        Uptime uptime = new Uptime();
        uptime.addUptime(new LogInterval(ofSeconds(6), new LogInstant(10000)));

        assertThat(uptime.at(Instant.ofEpochMilli(3000)), nullValue());
        assertThat(uptime.at(Instant.ofEpochMilli(4000)), equalTo(ofSeconds(0)));
        assertThat(uptime.at(Instant.ofEpochMilli(5000)), equalTo(ofSeconds(1)));
        assertThat(uptime.at(Instant.ofEpochMilli(10000)), equalTo(ofSeconds(6)));
        assertThat(uptime.at(Instant.ofEpochMilli(11000)), equalTo(ofSeconds(7)));
    }
}
