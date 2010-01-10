import glug.parser.logmessages.LogMessageParser
import glug.model.SignificantInterval
import java.util.regex.Matcher
import glug.model.ThreadModel
import glug.model.time.LogInstant
import org.joda.time.Duration
import glug.parser.logmessages.SomethingGroovy

//def durInMil = { new Duration(Integer.parseInt(durationInMillisText)) }
/*
new SomethingGroovy(
    eventType: 'Page Request',
    pattern:'Request for ([^ ]+?) completed in (\d+?) ms$',
    data: [ "Page Path" : { group(1) } ],
    duration: { durInMil(group(2)) },
    logger:[
      'com.gu.r2.common.webutil.RequestLoggingFilter',
      'com.gu.performance.diagnostics.requestlogging.RequestLoggingFilter',
      'com.gu.management.logging.RequestLoggingFilter'
    ]
)

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


def databaseQuery = [
  eventType: 'DB Query',
  pattern:'Query "(.+?)" \(component: (.+?)\) completed in (\d+?) ms',
  data: [ Query: { group(1) }, Component: { group(2) } ],
  duration: { durInMil(group(3)) },
  logger:[ 'com.gu.r2.common.diagnostic.database.PreparedStatementProxy' ]
]

def endecaRelatedContentQuery = [
  eventType: 'Endeca Related Content Query',
  pattern:'Endeca Query completed in (\d+?) ms',
  duration: { durInMil(group(1)) },
  logger:[ 'com.gu.r2.common.model.page.repository.endeca.EndecaRelatedContentPagesQueryDAO' ]
]

def pluckCall = [
  eventType: 'Pluck Call',
  pattern:'(\w+) .* completed in (\d+?) ms',
  data: [ Activity: { group(1) } ],
  duration: { durInMil(group(2)) },
  logger:[ 'com.gu.pluck.http.PluckHttpClient' ]
]

  SignificantInterval process(Matcher matcher, ThreadModel threadModel, LogInstant logInstant) {
    return null;
  }


2010-01-07 00:02:32,153 [resin-tcp-connection-*:8500-237] INFO  com.gu.pluck.http.PluckHttpClient - STREAM_RENDERED_COMMENTS 337109213 Pagination{numberPerPage=50, pageNumber=1} completed in 67 ms


2009-12-18 00:00:08,847 [resin-tcp-connection-*:8080-2997] INFO  com.gu.r2.common.model.page.repository.endeca.EndecaRelatedContentPagesQueryDAO - Endeca Query completed in 21 ms

2009-12-22 13:40:08,401 [resin-tcp-connection-*:8500-6925] INFO  com.gu.management.logging.RequestLoggingFilter - Request for /apps/pluck/Guardian/raw/getUserProfile?userId=2707368&callback=&at=u%3D2707368%26a%3Ddoesnotexist%26t%3D1261384013%26e%3Drwexcell%2540gmail%252ecom%26h%3Da69357104307ddc271c6ed0828ac5ff8 completed in 65 ms
2009-12-22 13:40:09,046 [resin-tcp-connection-*:8500-6551] INFO  com.gu.management.logging.RequestLoggingFilter - Request for /apps/pluck/Guardian/resources/postComment?at=u%3D3851792%26a%3DWilliamDean%26t%3D1261397771%26e%3Dsean%252emcgarraghy%2540gmail%252ecom%26h%3D4f91c7b7b8f51d51bd8fcfe3c18e5134 completed in 742 ms

   */