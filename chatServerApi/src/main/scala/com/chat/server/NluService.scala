package com.chat.server

import com.chat.server.ChatServer.{ChatRequest, ChatResponse}

import scala.concurrent.{ExecutionContext, Future}

object NluService {

  case class IntentResponse(intentName: String)

  def findIntent(chatRequest: ChatRequest)(implicit executionContext: ExecutionContext): Future[IntentResponse] = Future {
    if (chatRequest.message.contains("coffee"))
      IntentResponse("NearbyCoffeeIntent")
    else
      IntentResponse("UnrecognisedIntent")
  }
}
