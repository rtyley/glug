import glug.groovy.*;

register(
  new ParserDef(
    eventType: 'Page Request',
    pattern:~/Request for ([^ ]+?) completed in (\d+?) ms$/,
    data: {[ "Page Path": it.group(1) ]},
    duration: { durationInMillis(it.group(2)) },
    logger: [
      'com.gu.r2.common.webutil.RequestLoggingFilter',
      'com.gu.performance.diagnostics.requestlogging.RequestLoggingFilter',
      'com.gu.management.logging.RequestLoggingFilter'
    ]
  ),
  new ParserDef(
    eventType: 'Pluck Call',
    pattern:~/(\w+) .* completed in (\d+?) ms/,
    data: { [Activity: it.group(1)] },
    duration: { durationInMillis(it.group(2)) },
    logger:[ 'com.gu.pluck.http.PluckHttpClient' ]
  )
)
