name := "chatServerParent"
organization in ThisBuild := "com.chat.server"
scalaVersion in ThisBuild := "2.12.5"

lazy val chatServerParent = project
  .in(file("."))
  .settings(settings)
  .aggregate(chatServerSchema, chatServerApi)

lazy val chatServerSchema =
  project.settings(name := "chatServerSchema",
                   settings,
                   apiSchemaAssemblySettings,
                   libraryDependencies ++= commonDependencies)

val NettyVersion = "4.1.16.Final"

lazy val chatServerApi = project
  .settings(
    name := "chatServerApi",
    settings,
    apiAssemblySettings,
    libraryDependencies ++= Seq(
      "com.github.finagle" %% "finch-core" % "0.18.0"
        exclude ("io.netty", "netty-transport-native-unix-common")
        exclude ("io.netty", "netty-codec-http")
        exclude ("io.netty", "netty-codec-http2")
        exclude ("io.netty", "netty-codec")
        exclude ("io.netty", "netty-transport")
        exclude ("io.netty", "netty-buffer")
        exclude ("io.netty", "netty-common")
        exclude ("io.netty", "netty-resolver")
        exclude ("io.netty", "netty-handler")
        exclude ("io.netty", "netty-tcnative-boringssl-static")
        exclude ("io.netty", "netty-transport-native-epoll")
        exclude ("io.netty", "netty-handler-proxy"),
      "com.github.finagle" %% "finch-circe" % "0.18.0",
      "io.netty" % "netty-codec" % NettyVersion,
      "io.netty" % "netty-codec-http" % NettyVersion,
      "io.netty" % "netty-codec-http2" % NettyVersion,
      "io.netty" % "netty-transport" % NettyVersion,
      "io.netty" % "netty-buffer" % NettyVersion,
      "io.netty" % "netty-common" % NettyVersion,
      "io.netty" % "netty-resolver" % NettyVersion,
      "io.netty" % "netty-handler" % NettyVersion,
      "io.netty" % "netty-handler-proxy" % NettyVersion,
      "io.netty" % "netty-transport-native-unix-common" % NettyVersion,
      "io.netty" % "netty-transport-native-epoll" % NettyVersion,
      "io.netty" % "netty-tcnative-boringssl-static" % "2.0.6.Final",
      "io.netty" % "netty-codec-socks" % NettyVersion,
      "io.swagger" % "swagger-codegen" % "2.3.1",
      "io.circe" %% "circe-generic" % "0.9.3",
      "com.typesafe" % "config" % "1.3.3",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    )
  )
  //.enablePlugins(SwaggerCodegenPlugin)
  .dependsOn(chatServerSchema)

//lazy val dependencies =
//  new {
//    val logbackV = "1.2.3"
//    val slf4jV = "1.7.25"
//
//    val logback = "ch.qos.logback" % "logback-classic" % logbackV
//    val slf4j = "org.slf4j" % "jcl-over-slf4j" % slf4jV
//  }

lazy val commonDependencies = Seq()

lazy val settings =
  commonSettings ++
    wartremoverSettings ++
    scalafmtSettings

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val wartremoverSettings = Seq(
  wartremoverWarnings in (Compile, compile) ++= Warts.allBut(Wart.Throw))

lazy val scalafmtSettings = Seq(scalafmtOnCompile := true,
                                scalafmtTestOnCompile := true,
                                scalafmtVersion := "1.2.0")

lazy val apiAssemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  mainClass in assembly := Some("com.chat.server.ChatServer"),
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case _                             => MergeStrategy.first
  }
)

lazy val apiSchemaAssemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar")
