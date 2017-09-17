package pl.mwt.mtrs.http

import akka.http.scaladsl.model.StatusCodes
import pl.mwt.mtrs.http.model.MovieId
import pl.mwt.mtrs.svc.Reservation

private[http]
trait MovieTicketReservation
    extends MovieEndpoint {

  override def movieRoute =
    super.movieRoute ~
    pathSuffix("ticket") {
      post {
        entity(as[MovieId]) { movieRef =>
          onSuccess(movieService.reserveSeat(movieRef)) {
            case Reservation.Accepted =>
              complete(StatusCodes.OK)

            case Reservation.NoMovie =>
              complete(StatusCodes.Forbidden, "No such movie")

            case Reservation.NoSeats =>
              complete(StatusCodes.Forbidden, "No more available seats")
          }
        }
      }
    }
}
