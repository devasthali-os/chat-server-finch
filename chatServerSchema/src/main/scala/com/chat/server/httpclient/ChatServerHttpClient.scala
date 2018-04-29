package com.chat.server.httpclient

//import java.net.URL

import com.chat.server.schema.{
  ChatInitResponse,
  ChatResponse,
  HeartbeatResponse
}
//import com.twitter.util.Future
//import featherbed.circe._
//import io.circe.generic.auto._

//
//trait ChatServerEndpoints {
//  def heartbeat: Future[HeartbeatResponse]
//
//  def chatInit: Future[ChatInitResponse]
//
//  def chat: Future[ChatResponse]
//}
//
//class ChatServerHttpClient(baseResourceLocator: String) extends ChatServerEndpoints {
//
//  private val client = new featherbed.Client(new URL(baseResourceLocator))
//
//  private val heartbeatDef = client.get("heartbeat").accept("application/json")
//
//  def heartbeat: Future[HeartbeatResponse] = heartbeatDef.send[HeartbeatResponse]()
//
//  override def chatInit: Future[ChatInitResponse] = client.get("init")
//    .withHeader("x-correlation-id", "")
//    .withHeader("x-version", "")
//    .accept("application/json").send[ChatInitResponse]()
//
//  override def chat: Future[ChatResponse] = client.post("chat")
//    .withHeader("x-correlation-id", "")
//    .withHeader("x-user", "")
//    .withHeader("x-version", "")
//    .send[ChatResponse]()
//}
