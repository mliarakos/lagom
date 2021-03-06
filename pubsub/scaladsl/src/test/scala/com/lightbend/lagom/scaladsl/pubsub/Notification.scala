/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.lagom.scaladsl.pubsub

import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import play.api.libs.json.Json

case class Notification(msg: String) extends Serializable

object NotificationJsonSerializer extends JsonSerializerRegistry {
  override def serializers = Vector(
    JsonSerializer(Json.format[Notification])
  )
}
