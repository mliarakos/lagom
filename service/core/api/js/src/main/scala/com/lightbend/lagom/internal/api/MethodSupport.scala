/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.lagom.internal.api

import scala.scalajs.js

private[lagom] trait MethodSupport {

  /**
   * A JavaScript stand-in for [[java.lang.reflect.Method]].
   *
   * @param clazz The class that declared the method.
   * @param name The name of the method.
   */
  protected class Method private[lagom] (clazz: Class[_], name: String) {
    def getDeclaringClass: Class[_]              = clazz
    def getName: String                          = name
    def invoke(obj: Any, args: AnyRef*): Dynamic = obj.asInstanceOf[js.Dynamic].applyDynamic(name)(args)
  }

  protected def locateMethod(clazz: Class[_], name: String): Method = {
    new Method(clazz, name)
  }

}
