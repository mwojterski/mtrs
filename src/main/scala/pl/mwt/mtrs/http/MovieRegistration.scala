package pl.mwt.mtrs.http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.headers.Location
import pl.mwt.mtrs.http.model.Movie
import pl.mwt.mtrs.svc.{Accepted, Rejected}

private[http]
trait MovieRegistration
    extends MovieEndpoint {

  override def movieRoute =
    super.movieRoute ~
    pathEnd {
      post {
        entity(as[Movie]) { movie =>
          onSuccess(movieService.register(movie)) {
            case Accepted =>
              extractUri { uri =>
                val movieUri = uri withQuery Query("imdbId" -> movie.imdbId, "screenId" -> movie.screenId)
                complete(StatusCodes.Created, Location(movieUri) :: Nil, "Movie registered")
              }

            case Rejected(msg) => complete(StatusCodes.Forbidden, msg)
          }
        }
      }
    }
}
