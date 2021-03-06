package com.github.http4s.test

import javax.servlet.{DispatcherType, Filter}
import javax.servlet.http.HttpServlet
import java.util.EnumSet
import org.eclipse.jetty.servlet._

object JettyContainer {
  private val DefaultDispatcherTypes: EnumSet[DispatcherType] =
    EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC)
}

trait JettyContainer extends Container {
  import JettyContainer._

  def servletContextHandler: ServletContextHandler

  def addServlet(servlet: HttpServlet, path: String) =
    servletContextHandler.addServlet(new ServletHolder(servlet), path)

  def addServlet(servlet: Class[_ <: HttpServlet], path: String) =
    servletContextHandler.addServlet(servlet, path)

  def addFilter(filter: Filter, path: String): FilterHolder =
    addFilter(filter, path, DefaultDispatcherTypes)

  def addFilter(filter: Filter, path: String, dispatches: EnumSet[DispatcherType]): FilterHolder = {
    val holder = new FilterHolder(filter)
    servletContextHandler.addFilter(holder, path, dispatches)
    holder
  }

  def addFilter(filter: Class[_ <: Filter], path: String): FilterHolder =
    addFilter(filter, path, DefaultDispatcherTypes)

  def addFilter(filter: Class[_ <: Filter], path: String, dispatches: EnumSet[DispatcherType]): FilterHolder =
    servletContextHandler.addFilter(filter, path, dispatches)

  // Add a default servlet.  If there is no underlying servlet, then
  // filters just return 404.
  addServlet(classOf[DefaultServlet], "/")

}
