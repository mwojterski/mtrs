package pl.mwt.mtrs.http

import akka.http.scaladsl.model.StatusCodes
import pl.mwt.mtrs.http.model.MovieRef
import pl.mwt.mtrs.svc.{Accepted, Rejected}

private[http]
trait MovieTicketReservation
    extends MovieEndpoint {

  override def movieRoute =
    super.movieRoute ~
    pathSuffix("ticket") {
      post {
        entity(as[MovieRef]) { movieRef =>
          onSuccess(movieService.reserveSeat(movieRef)) {
            case Accepted =>
              complete(StatusCodes.OK)

            case Rejected(msg) =>
              complete(StatusCodes.Forbidden, msg)
          }
        }
      }
    }
}
