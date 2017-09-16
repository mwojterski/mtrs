package pl.mwt.mtrs.http

import akka.http.scaladsl.model.StatusCodes
import pl.mwt.mtrs.http.model.MovieRef

private[http]
trait MovieInformation
    extends MovieEndpoint {

  override def movieRoute =
    super.movieRoute ~
    pathEnd {
      get {
        parameters('imdbId, 'screenId).as(MovieRef.apply) { movieRef =>
          onSuccess(movieService.getInformation(movieRef)) { info =>
            complete(StatusCodes.OK, info)
          }
        }
      }
    }
}
