/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.lagom.internal.javadsl.client

import com.lightbend.lagom.internal.client.WebClient
import javax.inject.Inject
import javax.inject.Singleton
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext

@Singleton
class JavadslWebClient @Inject()(ws: WSClient)(implicit ec: ExecutionContext) extends WebClient(ws)(ec)
