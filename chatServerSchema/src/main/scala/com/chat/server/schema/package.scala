package com.chat.server.schema

final case class ChatRequest(correlationID: String, message: String)

final case class ChatResponse(correlationID: String,
                              displayText: String,
                              version: String)

final case class HeartbeatResponse(appName: String,
                                   appVersion: String,
                                   appEnvironment: String)

trait InitResponse

final case class ChatInitResponse(correlationID: String, message: String)
    extends InitResponse

final case class InvalidResponse(error: String) extends InitResponse
