
organization := "pl.mwt"
name := "mtrs"
version := "0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= {

  val akkaVer = "10.0.9"

  // AKKA
  Seq(
    "akka-http",
    "akka-http-spray-json"
  ).map {
    "com.typesafe.akka" %% _ % akkaVer
  } ++
  // MISCELLANEOUS
  Seq(
    "com.typesafe"                % "config"               % "1.3.1",

    "ch.qos.logback"              % "logback-classic"      % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging"        % "3.7.2",

    "io.getquill"                %% "quill-async-postgres" % "1.3.0"
  ) ++
  // TEST
  Seq(
    "specs2-core",
    "specs2-mock"
  ).map {
    "org.specs2" %% _ % "3.9.5"
  } ++
  Seq(
    "com.typesafe.akka" %% "akka-http-testkit" % akkaVer,
    "org.mockito"        % "mockito-all"       % "1.10.19"
  ).map {
    _ % "test"
  }
}