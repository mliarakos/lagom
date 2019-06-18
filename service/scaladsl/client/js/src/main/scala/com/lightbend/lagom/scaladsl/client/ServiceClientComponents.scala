/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.lagom.scaladsl.client

import java.io.File

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.Materializer
import com.lightbend.lagom.internal.scaladsl.api.broker.TopicFactoryProvider
import com.lightbend.lagom.internal.scaladsl.client.ScaladslServiceClient
import com.lightbend.lagom.internal.scaladsl.client.ScaladslServiceResolver
import com.lightbend.lagom.internal.scaladsl.client.ScaladslWebClient
import com.lightbend.lagom.internal.scaladsl.client.ScaladslWebSocketClient
import com.lightbend.lagom.scaladsl.api._
import com.lightbend.lagom.scaladsl.api.deser.DefaultExceptionSerializer
import com.lightbend.lagom.scaladsl.api.deser.ExceptionSerializer
import play.api.Configuration
import play.api.Environment
import play.api.Mode

import scala.concurrent.ExecutionContext
import scala.language.experimental.macros

/**
 * The Lagom service client components.
 */
trait LagomServiceClientComponents extends TopicFactoryProvider { self: LagomConfigComponent =>
  def serviceInfo: ServiceInfo
  def serviceLocator: ServiceLocator
  def materializer: Materializer
  def actorSystem: ActorSystem
  def executionContext: ExecutionContext
  def environment: Environment

  lazy val serviceResolver: ServiceResolver                = new ScaladslServiceResolver(defaultExceptionSerializer)
  lazy val defaultExceptionSerializer: ExceptionSerializer = new DefaultExceptionSerializer(environment)

  lazy val scaladslWebClient: ScaladslWebClient             = new ScaladslWebClient()(executionContext)
  lazy val scaladslWebSocketClient: ScaladslWebSocketClient = new ScaladslWebSocketClient()(executionContext)
  lazy val serviceClient: ServiceClient = new ScaladslServiceClient(
    scaladslWebClient,
    scaladslWebSocketClient,
    serviceInfo,
    serviceLocator,
    serviceResolver,
    optionalTopicFactory
  )(executionContext, materializer)
}

/**
 * Convenience for constructing service clients in a non Lagom server application.
 *
 * It is important to invoke [[#stop]] when the application is no longer needed, as this will trigger the shutdown
 * of all thread and connection pools.
 */
abstract class LagomClientApplication(
    clientName: String,
    classLoader: ClassLoader = new ClassLoader() {}
) extends LagomServiceClientComponents
    with LagomConfigComponent {
  override lazy val serviceInfo: ServiceInfo = ServiceInfo(clientName, Map.empty)
  override lazy val environment: Environment = Environment(new File("."), classLoader, Mode.Prod)
  // TODO: load configuration
  lazy val configuration: Configuration = Configuration.empty

  // TODO: create compatibility layer for ActorSystemProvider?
  lazy val actorSystem: ActorSystem = ActorSystem("default", configuration.underlying, environment.classLoader)

  override lazy val materializer: Materializer         = ActorMaterializer.create(actorSystem)
  override lazy val executionContext: ExecutionContext = actorSystem.dispatcher

  /**
   * Stop the application.
   */
  def stop(): Unit = actorSystem.terminate()
}
