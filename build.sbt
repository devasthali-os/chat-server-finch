lazy val root = (project in file("."))
  .aggregate(chatServerApi, chatServerSchema)

lazy val chatServerSchema = project
lazy val chatServerApi = project.dependsOn(chatServerSchema)