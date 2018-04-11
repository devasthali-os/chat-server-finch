package com.chat.server

import com.twitter.finagle.Http
import com.twitter.util.Await

import io.finch._
import io.finch.circe._
import io.finch.syntax._
import io.circe.generic.auto._

object ChatServer {

  def main(args: Array[String]): Unit = {

    case class ChatRequest(correlationID: String, message: String)
    case class ChatResponse(correlationID: String, displayText: String)

    val chat: Endpoint[ChatResponse] =
      post("chat" :: jsonBody[ChatRequest]) { chatRequest: ChatRequest =>

        Ok(ChatResponse(chatRequest.correlationID, "Hi, How can I help you?"))
      }

    Await.ready(Http.server.serve(":9090", chat.toService))

  }
}
