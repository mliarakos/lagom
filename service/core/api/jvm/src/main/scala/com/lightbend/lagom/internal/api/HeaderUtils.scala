/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.lagom.internal.api

import java.util.Locale

object HeaderUtils {

  /**
   * Normalize an HTTP header name.
   *
   * This normalization is abstracted into a method so that it can be
   * implemented differently on the JVM and in JS. This JVM implementation
   * uses [[java.util.Locale]] to ensue the proper locale, rather then using
   * the client locale.
   */
  @inline
  def normalize(name: String): String =
    name.toLowerCase(Locale.ENGLISH)

}
