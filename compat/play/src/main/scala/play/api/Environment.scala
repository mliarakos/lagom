/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

/*
 * Skeleton of [[play.api.Environment]] removing all functionality for JS compatibility.
 */

package play.api

import java.io.File

case class Environment(rootPath: File, classLoader: ClassLoader, mode: Mode)

object Environment {
  def simple(path: File = new File("."), mode: Mode = Mode.Test) =
    Environment(path, new ClassLoader() {}, mode)
}
