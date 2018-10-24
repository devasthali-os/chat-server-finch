package com.chat.server

import java.util.UUID

import cats.effect.IO

//import com.chat.server.httpclient.ChatServerEndpoints
import com.chat.server.schema.{
  ChatInitResponse,
  ChatRequest,
  ChatResponse,
  HeartbeatResponse
}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Await, Future}
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import io.finch.catsEffect._

trait ChatServer {
  def chat(user: String,
           version: String,
           chatRequest: ChatRequest): concurrent.Future[ChatResponse]
}

object ChatServer extends ChatServer with Endpoint.Module[IO] {

  private val logger = LoggerFactory.getLogger("ChatServer")

  val heartbeat: Endpoint[IO, HeartbeatResponse] = get("heartbeat") {
    Ok(
      HeartbeatResponse(AppConfig.AppName,
                        AppConfig.AppVersion,
                        AppConfig.AppEnvironment)
    )
  }

//  def chatInitHeader: Endpoint[IO, String] =
//    header("x-correlation-id").mapOutputAsync(id => {
//      if (id == null) Ok("Invalid correlationID")
//      else Ok(UUID.randomUUID().toString)
//    })

  val chatInit: Endpoint[IO, ChatInitResponse] =
    get("chat" :: "init" :: header("x-correlation-id") :: header("x-version")) {
      (id: String, version: String) =>
        Ok(ChatInitResponse(id, "Hi, How can I help you?"))
          .withHeader("version", version)
    }

  import io.finch.Error.{NotPresent, NotValid}

  final case class History(id: String)
  trait ApiError
  final case class InvalidReq(id: String, description: String) extends ApiError
  final case class InternalApiError(id: String, description: String)
      extends ApiError

//  val chatHistory: Endpoint[IO, Either[InternalApiError, History]] =
//    get("chat" :: "history" :: param[String]("correlationId")) {
//      (correlationId: String) =>
//        Ok(Right(History(id = correlationId)))
//    } handle {
//      case e => Ok(Left[InternalApiError, History](InternalApiError("a", "b")))
//    }

  def chat(user: String,
           version: String,
           chatRequest: ChatRequest): concurrent.Future[ChatResponse] = {
    logger.info(s"request: $chatRequest")
    ChatPipeline.pipeline(chatRequest)
  }

  def main(args: Array[String]): Unit = {

    val chatEndpoint: Endpoint[IO, ChatResponse] =
      post("api" :: "chat" :: header("x-user").should("be present") {
        _.length > 5
      } :: header("x-client-version") :: jsonBody[ChatRequest]) {
        (user: String, version: String, chatRequest: ChatRequest) =>
          val chatResp: concurrent.Future[ChatResponse] =
            chat(user, version, chatRequest)

          IO.fromFuture(IO(chatResp)).map { res =>
            logger.info(s"response: $res")
            Ok(res)
          }
      }

    val endpoints: Service[Request, Response] =
      (heartbeat :+: chatInit :+: chatEndpoint).toService

    Await.ready(Http.server.serve(":9090", endpoints))

  }
}
