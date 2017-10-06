
lazy val baseSettings = Seq(
  scalaVersion := "2.12.3",
  libraryDependencies ++= Seq(
    "org.mockito" % "mockito-all" % "1.10.19" % "test",
    "org.hamcrest" % "hamcrest-all" % "1.3" % "test",
    "junit" % "junit" % "4.12" % "test"
  )

)

lazy val `glug-model` = project.in(file("glug-model")).settings(baseSettings: _*)

lazy val `glug-gui-components` = project.in(file("glug-gui-components")).settings(baseSettings: _*).dependsOn(`glug-model`)

lazy val `glug-gui` = project.in(file("glug-gui")).settings(baseSettings: _*).dependsOn(`glug-model`, `glug-gui-components`)


lazy val root = (project in file(".")).aggregate(`glug-model`, `glug-gui-components`, `glug-gui`)
