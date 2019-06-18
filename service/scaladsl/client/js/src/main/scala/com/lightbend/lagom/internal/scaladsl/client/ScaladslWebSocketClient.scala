/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.lagom.internal.scaladsl.client

import com.lightbend.lagom.internal.client.WebSocketClient

import scala.concurrent.ExecutionContext

private[lagom] class ScaladslWebSocketClient()(implicit ec: ExecutionContext)
    extends WebSocketClient()(ec)
    with ScaladslServiceApiBridge
