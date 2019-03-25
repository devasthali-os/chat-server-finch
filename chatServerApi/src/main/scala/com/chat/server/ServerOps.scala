package com.chat.server

import cats.data.Kleisli
import cats.effect.IO
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.{Base64StringEncoder, Future}

object ServerOps {

  final class AuthOps extends SimpleFilter[Request, Response] {

    private[this] def authenticated(req: Request): Future[Boolean] =
      req.headerMap.get("x-api-key") match {
        case Some(headerValue) if headerValue == "api-secret" => Future.True
        case _                                                => Future.False
      }

    def apply(req: Request, s: Service[Request, Response]): Future[Response] =
      authenticated(req).flatMap {
        case true  => s(req)
        case false => Future.value(Response(Status.Unauthorized))
      }
  }

  def server: AuthOps = new AuthOps

  val authFilter = Kleisli.ask[IO, Request].andThen { req =>
    req.headerMap.get("x-api-key") match {
      case Some(headerValue) if headerValue == "api-secret" => IO.pure(req)
      case _                                                => IO.raiseError(new IllegalStateException())
    }
  }
}
