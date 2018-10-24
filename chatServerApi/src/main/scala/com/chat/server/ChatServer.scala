package com.chat.server

import java.util.UUID

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
import io.finch.syntax._
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import io.finch.syntax.scalaFutures._

trait ChatServer {
  def chat(user: String,
           version: String,
           chatRequest: ChatRequest): concurrent.Future[ChatResponse]
}

object ChatServer extends ChatServer {

  private val logger = LoggerFactory.getLogger("ChatServer")

  val heartbeat: Endpoint[HeartbeatResponse] = get("heartbeat") {
    scala.concurrent.Future.successful(
      Ok(
        HeartbeatResponse(AppConfig.AppName,
                          AppConfig.AppVersion,
                          AppConfig.AppEnvironment)))
  }

  def chatInitHeader: Endpoint[String] =
    header("x-correlation-id").mapOutputAsync(id => {
      if (id == null) Future.value(Ok("Invalid correlationID"))
      else Future.value(Ok(UUID.randomUUID().toString))
    })

  val chatInit: Endpoint[ChatInitResponse] =
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

  val chatHistory: Endpoint[Either[Int, History]] =
    get("chat" :: "history" :: param[String]("correlationId")) {
      (correlationId: String) =>
        Ok(Right(History(id = correlationId)))
    } handle {
      case e => Ok(Left(-1))
    }

  def chat(user: String,
           version: String,
           chatRequest: ChatRequest): concurrent.Future[ChatResponse] = {
    logger.info(s"request: $chatRequest")
    ChatPipeline.pipeline(chatRequest)
  }

  def main(args: Array[String]): Unit = {

    val chatEndpoint: Endpoint[ChatResponse] =
      post("api" :: "chat" :: header("x-user").should("be present") {
        _.length > 5
      } :: header("x-client-version") :: jsonBody[ChatRequest]) {
        (user: String, version: String, chatRequest: ChatRequest) =>
          val chatResp: concurrent.Future[ChatResponse] =
            chat(user, version, chatRequest)

          chatResp.map(Ok) andThen {
            case Success(s) => logger.info(s"response: $s")
            case Failure(e) => logger.info(s"error: $e")
          }
      }

    val endpoints: Service[Request, Response] =
      (heartbeat :+: chatInit :+: chatHistory :+: chatEndpoint).toService

    Await.ready(Http.server.serve(":9090", endpoints))

  }
}
