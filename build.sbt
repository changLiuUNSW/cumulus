name := """cumulus"""

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  cache,
  ws,
  evolutions,
  jdbc,
  "org.postgresql" % "postgresql" % "9.4.1211",
  "com.typesafe.play" %% "anorm" % "2.5.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

