package com.chat.server

import java.util.UUID

import com.twitter.finagle.Http
import com.twitter.util.Await
import io.finch._
import io.finch.circe._
import io.finch.syntax._
import io.circe.generic.auto._
import com.twitter.util.Future

object ChatServer {

  def main(args: Array[String]): Unit = {

    case class ChatRequest(correlationID: String, message: String)
    case class ChatResponse(correlationID: String, displayText: String)

    trait InitResponse
    case class ChatInitResponse(correlationID: String, message: String) extends InitResponse
    case class InvalidResponse(error: String) extends InitResponse

    val chatInitHeader: Endpoint[String] =
      header("correlationID").mapOutputAsync(id => {
        if (id == null) Future.value(Ok("Invalid correlationID"))
        else Future.value(Ok(UUID.randomUUID().toString))
      })

    val chatInit: Endpoint[ChatInitResponse] = get("init" :: chatInitHeader) { id: String =>
      Ok(ChatInitResponse(id, "Hi, How can I help you?"))
    }

    val chat: Endpoint[ChatResponse] =
      post("chat" :: jsonBody[ChatRequest]) { chatRequest: ChatRequest =>

        Ok(ChatResponse(chatRequest.correlationID, "Here are some coffee shops"))
      }

    val endpoints = (chatInit :+: chat).toService

    Await.ready(Http.server.serve(":9090", endpoints))

  }
}
