/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

/*
 * Skeleton of [[play.api.Configuration]] removing all functionality for JS compatibility.
 */

package play.api

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object Configuration {

  /**
   * Returns an empty Configuration object.
   */
  def empty = Configuration(ConfigFactory.empty())

}

case class Configuration(underlying: Config)
