
organization := "pl.mwt"
name := "mtrs"
version := "0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= {

  // AKKA
  Seq(
    "akka-http",
    "akka-http-spray-json"
  ).map {
    "com.typesafe.akka" %% _ % "10.0.9"
  } ++
  // MISCELLANEOUS
  Seq(
    "com.typesafe" % "config" % "1.3.1",

    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",

    "io.getquill" %% "quill-async-postgres" % "1.3.0"
  )

}