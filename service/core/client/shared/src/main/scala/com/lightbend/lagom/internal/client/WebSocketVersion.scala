/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.lagom.internal.client

private[lagom] sealed trait WebSocketVersion

private[lagom] object WebSocketVersion {
  case object V00 extends WebSocketVersion
  case object V07 extends WebSocketVersion
  case object V08 extends WebSocketVersion
  case object V13 extends WebSocketVersion
}
