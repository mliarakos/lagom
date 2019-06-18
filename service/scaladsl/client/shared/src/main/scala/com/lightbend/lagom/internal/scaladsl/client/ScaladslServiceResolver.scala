/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.lagom.internal.scaladsl.client

import com.lightbend.lagom.internal.scaladsl.api.ScaladslPath
import com.lightbend.lagom.scaladsl.api.Descriptor.Call
import com.lightbend.lagom.scaladsl.api.Descriptor.RestCallId
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.api.ServiceAcl
import com.lightbend.lagom.scaladsl.api.deser.DefaultExceptionSerializer
import com.lightbend.lagom.scaladsl.api.deser.ExceptionSerializer
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.client.ServiceResolver

private[lagom] class ScaladslServiceResolver(defaultExceptionSerializer: ExceptionSerializer) extends ServiceResolver {
  override def resolve(descriptor: Descriptor): Descriptor = {
    val withExceptionSerializer: Descriptor =
      if (descriptor.exceptionSerializer == DefaultExceptionSerializer.Unresolved) {
        descriptor.withExceptionSerializer(defaultExceptionSerializer)
      } else descriptor

    val withAcls: Descriptor = {
      val acls = descriptor.calls.collect {
        case callWithAutoAcl if callWithAutoAcl.autoAcl.getOrElse(descriptor.autoAcl) =>
          val pathSpec = ScaladslPath.fromCallId(callWithAutoAcl.callId).regex.regex
          val method   = calculateMethod(callWithAutoAcl)
          ServiceAcl(Some(method), Some(pathSpec))
      }

      if (acls.nonEmpty) {
        withExceptionSerializer.addAcls(acls: _*)
      } else withExceptionSerializer
    }

    val withCircuitBreakers = {
      // iterate all calls and replace those where CB is None with their setup or the default.
      val callsWithCircuitBreakers: Seq[Call[_, _]] = descriptor.calls.map { call =>
        val circuitBreaker = call.circuitBreaker.getOrElse(descriptor.circuitBreaker)
        call.withCircuitBreaker(circuitBreaker)
      }
      withAcls.withCalls(callsWithCircuitBreakers: _*)
    }

    withCircuitBreakers
  }

  private def calculateMethod(serviceCall: Descriptor.Call[_, _]): Method = {
    serviceCall.callId match {
      case rest: RestCallId => rest.method
      case _                =>
        // If either the request or the response serializers are streamed, then WebSockets will be used, in which case
        // the method must be GET
        if (serviceCall.requestSerializer.isStreamed || serviceCall.responseSerializer.isStreamed) {
          Method.GET
          // Otherwise, if the request serializer is used, we default to POST
        } else if (serviceCall.requestSerializer.isUsed) {
          Method.POST
        } else {
          // And if not, to GET
          Method.GET
        }
    }
  }

}
