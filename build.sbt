lazy val baseSettings = Seq(
  scalaVersion := "2.12.3"
)

lazy val `glug-model` = project.in(file("glug-model")).settings(baseSettings: _*)

lazy val `glug-gui-components` = project.in(file("glug-gui-components")).settings(baseSettings: _*).dependsOn(`glug-model`)

lazy val `glug-gui` = project.in(file("glug-gui")).settings(baseSettings: _*).dependsOn(`glug-model`, `glug-gui-components`)


lazy val root = (project in file(".")).aggregate(`glug-model`, `glug-gui-components`, `glug-gui`)
