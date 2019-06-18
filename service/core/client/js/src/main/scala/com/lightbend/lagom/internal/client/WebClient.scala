/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.lagom.internal.client

import java.nio.ByteBuffer

import akka.util.ByteString
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.ext.AjaxException
import play.api.http.HeaderNames

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

private[lagom] class WebClient()(implicit ec: ExecutionContext) {

  import WebClient._

  def request(
      method: String,
      url: String,
      headers: Map[String, String],
      body: Option[ByteString]
  ): Future[HttpResponse] = {
    // Remove User-Agent header because it's not allowed by XMLHttpRequest
    val filteredHeaders = headers - HeaderNames.USER_AGENT
    val data            = body.map(_.asByteBuffer).getOrElse(ByteBuffer.wrap(Array[Byte]()))

    Ajax
      .apply(
        method = method,
        url = url,
        data = data,
        timeout = 0,
        headers = filteredHeaders,
        withCredentials = false,
        responseType = ""
      )
      .recover({
        case AjaxException(xhr) => xhr
      })
      .map(xhr => {
        val text = Option(xhr.responseText).getOrElse("")
        HttpResponse(xhr.status, parseHeaders(xhr), ByteString.fromString(text))
      })
  }

}

private object WebClient {

  def parseHeaders(xhr: XMLHttpRequest): Map[String, Seq[String]] = {
    xhr
      .getAllResponseHeaders()
      .split("""\r\n""")
      .map(header => {
        val Array(key, values) = header.trim.split(":", 2)
        key -> values.trim.split(",").map(_.trim).toSeq
      })
      .toMap
  }

}
