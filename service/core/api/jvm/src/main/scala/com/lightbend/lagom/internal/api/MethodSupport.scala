/*
 * Copyright (C) 2016-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package com.lightbend.lagom.internal.api

private[lagom] trait MethodSupport {

  protected type Method = java.lang.reflect.Method

  protected def locateMethod(clazz: Class[_], name: String): Method = {
    // The class passed in may be an implementation (anonymous, implemented by a macro) of the service, and just doing
    // a simple getMethod on it may return a method whose declaring class is the implementation, which won't allow us
    // to invoke it. So we need to search the classes interfaces first for the method, and then if we don't find it
    // there, we'll search the class itself.
    val classes = clazz.getInterfaces.toSeq ++ Option(clazz.getSuperclass) :+ clazz
    classes
      .flatMap(_.getMethods)
      .find(_.getName == name)
      .getOrElse(throw new NoSuchMethodException(name))
  }

}
