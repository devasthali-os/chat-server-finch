package com.chat.server

import com.chat.server.ChatServer.{ChatRequest, ChatResponse}
import com.chat.server.NluService.IntentResponse

import scala.concurrent.{ExecutionContext, Future}

object ChatPipeline {

  def pipeline(chatRequest: ChatRequest)(implicit executionContext: ExecutionContext): Future[ChatResponse] = {
    NluService.findIntent(chatRequest).map {
      case IntentResponse("NearbyCoffeeIntent") => ChatResponse(chatRequest.correlationID, "Here are coffee shops", AppConfig.AppVersion)
      case _ => ChatResponse(chatRequest.correlationID, "Did not understand you", AppConfig.AppVersion)
    }

  }
}
