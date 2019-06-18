/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.lagom.internal.client

import akka.util.ByteString
import play.api.libs.ws.InMemoryBody
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

private[lagom] class WebClient(ws: WSClient)(implicit ec: ExecutionContext) {

  def request(
      method: String,
      url: String,
      headers: Map[String, String],
      body: Option[ByteString]
  ): Future[HttpResponse] = {
    val requestHolder = ws
      .url(url)
      .withHttpHeaders(headers.toSeq: _*)
      .withMethod(method)

    val requestWithBody = body.map(body => requestHolder.withBody(InMemoryBody(body))).getOrElse(requestHolder)

    requestWithBody.execute().map { response =>
      HttpResponse(response.status, response.headers, response.bodyAsBytes)
    }
  }

  def close(): Unit = ws.close()

}
