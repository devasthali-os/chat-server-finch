package com.chat.server

import com.chat.server.schema.ChatRequest

import scala.concurrent.{ExecutionContext, Future}

object NluService {

  final case class IntentResponse(intentName: String)

  def findIntent(chatRequest: ChatRequest)(
      implicit executionContext: ExecutionContext): Future[IntentResponse] =
    Future {
      if (chatRequest.message.contains("coffee"))
        IntentResponse("NearbyCoffeeIntent")
      else
        IntentResponse("UnrecognisedIntent")
    }
}
