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
  ),
  new ParserDef(
    pattern:~/(Http request for \w+) (.*) completed in (\d+?) ms/,
    data: { [type: it.group(1), params:urlParams(it.group(2))] },
    duration: { durationInMillis(it.group(3)) },
    logger:[ 'com.gu.r2.common.util.http.HttpConnection' ]
  ),
  new ParserDef(
    pattern:~/Endeca Query completed in (\d+?) ms/,
    data: { [type:'Endeca query'] },
    duration: { durationInMillis(it.group(1)) },
    logger:[ 'com.gu.r2.common.model.page.repository.endeca.EndecaRelatedContentPagesQueryDAO' ]
  ),
  new ParserDef(
    pattern:~/RSS request for (\w+) completed in (\d+?) ms/,
    data: { [type:'RSS data request', "Visitor": it.group(1)] },
    duration: { durationInMillis(it.group(2)) },
    logger:[ 'com.gu.r2.frontend.controller.page.rss.RssFeedFactory' ]
  )
)

