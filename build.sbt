name := "chat-server"

version := "0.1"

scalaVersion := "2.12.5"

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % "0.18.0",
  "com.github.finagle" %% "finch-circe" % "0.18.0",
  "io.circe" %% "circe-generic" % "0.9.3"
)