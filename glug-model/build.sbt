libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.9.9",
  "com.google.guava" % "guava" % "23.1-jre",
  "com.madgag" % "util-intervals" % "1.35",
  "org.codehaus.groovy" % "groovy" % "2.4.12",
  "com.lihaoyi" %% "fastparse" % "0.4.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)

resolvers += Resolver.sonatypeRepo("public")
