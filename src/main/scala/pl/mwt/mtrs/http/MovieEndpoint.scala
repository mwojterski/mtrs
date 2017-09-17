package pl.mwt.mtrs.http
import akka.http.scaladsl.server.Route
import pl.mwt.mtrs.svc.MovieService

private[http]
trait MovieEndpoint
    extends Endpoint {

  override final def routes =
    super.routes ~
    pathPrefix("movie") {
      movieRoute
    }

  def movieRoute: Route = reject

  def movieService: MovieService
}
