import glug.groovy.*;

register(
  new ParserDef(
    pattern:~/Request for ([^ ]+?) completed in (\d+?) ms$/,
    data: {[ type:'Page Request', "Page Path": it.group(1) ]},
    duration: { durationInMillis(it.group(2)) },
    logger: [
      'com.gu.r2.common.webutil.RequestLoggingFilter',
      'com.gu.performance.diagnostics.requestlogging.RequestLoggingFilter',
      'com.gu.management.logging.RequestLoggingFilter'
    ]
  ),
  new ParserDef(
    pattern:~/(\w+) .* completed in (\d+?) ms/,
    data: { [type:'Pluck Call',  Activity: it.group(1)] },
    duration: { durationInMillis(it.group(2)) },
    logger:[ 'com.gu.pluck.http.PluckHttpClient' ]
  )
)
