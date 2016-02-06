name := """EchoCave"""
version := "1.0"
scalaVersion := "2.11.7"

EclipseKeys.withSource := true
routesGenerator := InjectedRoutesGenerator

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "org.mongodb.scala" %% "mongo-scala-driver" % "1.0.1",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.9",
  "joda-time" % "joda-time" % "2.9.2",
  ws,
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"


