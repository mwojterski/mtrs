package pl.mwt.mtrs.http

import akka.http.scaladsl.model.StatusCodes
import pl.mwt.mtrs.http.model.{MovieId, MovieInfo}
import pl.mwt.mtrs.svc.{HasInfo, NoMovie}

private[http]
trait MovieInformation
    extends MovieEndpoint {

  override def movieRoute =
    super.movieRoute ~
    pathEnd {
      get {
        parameters('imdbId, 'screenId).as(MovieId.apply) { movieRef =>
          onSuccess(movieService.getInformation(movieRef)) {
              case NoMovie =>
                complete(StatusCodes.NotFound, "No such movie")

              case HasInfo(info) =>
                complete(StatusCodes.OK, info: MovieInfo)
          }
        }
      }
    }
}
