/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.lagom.internal.api

object HeaderUtils {

  /**
   * Normalize an HTTP header name.
   *
   * This normalization is abstracted into a method so that it can be
   * implemented differently on the JVM and in JS. This JS implementation
   * uses the JS `toLowerCase` method which ignores the client locale to
   * prevent any locale mapping issues.
   */
  @inline
  def normalize(name: String): String =
    name.toLowerCase

}
