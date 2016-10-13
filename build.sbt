name := "AkkaRestApi"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaStreamVersion = "2.0.5"
  val akkaVersion = "2.4.11"
  val scalaTestVersion       = "3.0.0"
  val scalaMockVersion       = "3.3.0"
  val slickVersion = "3.1.1"
  val circeV = "0.5.1"

  Seq(
    "com.typesafe.akka" %% "akka-actor"                           % akkaVersion,
    "com.typesafe.akka" %% "akka-stream-experimental"             % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-experimental"               % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-core-experimental"          % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental"    % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-testkit-experimental"       % akkaStreamVersion,
    "com.typesafe.slick"%% "slick"                                % slickVersion,
    "com.typesafe.play" %% "play-slick-evolutions"                % "2.0.2",
    "org.apache.hadoop"  % "hadoop-client"                         % "2.7.3",
    "com.typesafe.scala-logging" % "scala-logging_2.11"            % "3.5.0",
    "org.slf4j"          %  "slf4j-nop"                            % "1.6.4",
    "org.postgresql"     %  "postgresql"                           % "9.4-1201-jdbc41",
    "org.flywaydb"       %  "flyway-core"                          % "3.2.1",
    "com.typesafe.akka"  %% "akka-testkit"                         % akkaVersion % "test",
    "ru.yandex.qatools.embed" % "postgresql-embedded"              % "1.15" % "test",
    "org.scalatest"      %% "scalatest"                            % scalaTestVersion % "test",
    "org.scalamock"      %% "scalamock-scalatest-support"          % scalaMockVersion % "test",
    "com.typesafe.akka"  %% "akka-http-testkit-experimental"       % akkaStreamVersion
  )
}