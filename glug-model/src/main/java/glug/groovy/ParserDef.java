package glug.groovy;

import glug.parser.LoggedDurationExtractor;
import groovy.lang.Closure;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/*
def pageRequestParser = [
  eventType: 'Page Request',
  pattern:'Request for ([^ ]+?) completed in (\d+?) ms$',
  data: [ "Page Path" : { group(1) } ],
  duration: { durInMil(group(2)) },
  logger:[
    'com.gu.r2.common.webutil.RequestLoggingFilter',
    'com.gu.performance.diagnostics.requestlogging.RequestLoggingFilter',
    'com.gu.management.logging.RequestLoggingFilter'
  ]
]
 */
public class ParserDef {
    public Pattern pattern;
    public Closure data;
    public Closure duration;
    public Set<String> logger;
}
