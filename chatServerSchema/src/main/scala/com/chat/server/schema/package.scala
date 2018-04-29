package com.chat.server.schema

final case class ChatRequest(correlationID: String, message: String)

final case class ChatResponse(correlationID: String,
                              displayText: String,
                              version: String)
