package pl.mwt.mtrs.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{Directives, Route}

private[http]
trait Endpoint
  extends Directives
     with SprayJsonSupport {

  def routes: Route = reject
}
