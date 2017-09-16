package pl.mwt.mtrs

import akka.http.scaladsl.server.HttpApp
import pl.mwt.mtrs.svc._

object Application
    extends HttpApp
       with http.Api {

  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  override def movieService: MovieService = MovieServiceImpl
}
