name := """EchoCave"""
version := "1.0"
scalaVersion := "2.11.6"

EclipseKeys.withSource := true
routesGenerator := InjectedRoutesGenerator

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "org.mongodb.scala" %% "mongo-scala-driver" % "1.0.1",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.9",
  ws,
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"




fork in run := true