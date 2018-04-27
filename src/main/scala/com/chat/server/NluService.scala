package com.chat.server

import com.chat.server.ChatServer.{ChatRequest, ChatResponse}

object NluService {

  case class IntentResponse(intentName: String)

  def findIntent(chatRequest: ChatRequest): IntentResponse = {
    if (chatRequest.message.contains("coffee"))
      IntentResponse("NearbyCoffeeIntent")
    else
      IntentResponse("UnrecognisedIntent")
  }
}
