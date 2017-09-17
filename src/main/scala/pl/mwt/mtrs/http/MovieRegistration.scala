package pl.mwt.mtrs.http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.headers.Location
import pl.mwt.mtrs.http.model.MovieDef
import pl.mwt.mtrs.svc.Registration

private[http]
trait MovieRegistration
    extends MovieEndpoint {

  override def movieRoute =
    super.movieRoute ~
    pathEnd {
      post {
        entity(as[MovieDef]) { movie =>
          onSuccess(movieService.register(movie)) {
            case Registration.Accepted =>
              extractUri { uri =>
                val movieUri = uri withQuery Query("imdbId" -> movie.imdbId, "screenId" -> movie.screenId)
                complete(StatusCodes.Created, Location(movieUri) :: Nil, "Movie registered")
              }

            case Registration.Exists =>
              complete(StatusCodes.Forbidden, "Movie already exists")

            case Registration.InvalidImdbId =>
              complete(StatusCodes.BadRequest, "Invalid imdbId")
          }
        }
      }
    }
}
