package glug.parser.logmessages

//import glug.parser.logmessages.LogMessageParser
//import glug.model.SignificantInterval
//import java.util.regex.Matcher
//import glug.model.ThreadModel
//import glug.model.time.LogInstant
//import org.joda.time.Duration
//import glug.parser.logmessages.SomethingGroovy
//import java.util.regex.MatchResult
//import glug.parser.LoggedDurationExtractor
//
//def durInMillisFrom = { String s -> return new Duration(Integer.parseInt(s)) }


registerParser(new SomethingGroovy(
  eventType: 'Page Request',
  pattern:~/Request for ([^ ]+?) completed in (\d+?) ms$/,
  data: [ "Page Path" : { it.group(1) } ],
  duration: { durInMillisFrom(it.group(2)) },
  logger: [
    'com.gu.r2.common.webutil.RequestLoggingFilter',
    'com.gu.performance.diagnostics.requestlogging.RequestLoggingFilter',
    'com.gu.management.logging.RequestLoggingFilter'
  ]
))
