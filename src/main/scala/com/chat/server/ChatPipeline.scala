package com.chat.server

import com.chat.server.ChatServer.{ChatRequest, ChatResponse}
import com.chat.server.NluService.IntentResponse

object ChatPipeline {

  def pipeline(chatRequest: ChatRequest): ChatResponse ={
    NluService.findIntent(chatRequest) match {
      case IntentResponse("NearbyCoffeeIntent") => ChatResponse(chatRequest.correlationID, "Here are coffee shops")
      case _ => ChatResponse(chatRequest.correlationID, "Did not understand you")
    }

  }
}
