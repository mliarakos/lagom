/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

/*
 * Implementation of [[play.core.Execution.trampoline]] for JS compatibility.
 * Omitted all other objects in the source file.
 */

package play.core

private[play] object Execution {
  def trampoline = scala.scalajs.concurrent.JSExecutionContext.queue
}
