/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

/*
 * Implementation of [[play.api.Mode]] removing the Java interoperability for JS compatibility.
 * Omitted all other objects in the source file.
 */

package play.api

/**
 * Application mode, either `Dev`, `Test`, or `Prod`.
 *
 * @see [[play.Mode]]
 */
sealed abstract class Mode

object Mode {

  case object Dev  extends Mode
  case object Test extends Mode
  case object Prod extends Mode

  lazy val values: Set[Mode] = Set(Dev, Test, Prod)
}
