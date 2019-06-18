/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.lagom.internal.scaladsl.client

import com.lightbend.lagom.internal.client.WebClient
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext

private[lagom] class ScaladslWebClient(ws: WSClient)(implicit ec: ExecutionContext) extends WebClient(ws)(ec)
