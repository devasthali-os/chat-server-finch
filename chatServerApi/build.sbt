name := "chatServerApi"

version := "0.1"

scalaVersion := "2.12.5"

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % "0.18.0",
  "com.github.finagle" %% "finch-circe" % "0.18.0",
  "io.circe" %% "circe-generic" % "0.9.3",
  "com.typesafe" % "config" % "1.3.3",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

resolvers += "central" at "http://central.maven.org/maven2/"

assemblyJarName in assembly := "chatServerApi.jar"

mainClass in assembly := Some("com.chat.server.ChatServer")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
