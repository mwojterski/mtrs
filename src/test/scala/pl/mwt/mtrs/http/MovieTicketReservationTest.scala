package pl.mwt.mtrs.http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.Specs2RouteTest
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import pl.mwt.mtrs.http.model.MovieId
import pl.mwt.mtrs.shared.FutureUtil.immediateSuccess
import pl.mwt.mtrs.svc.{MovieService, Reservation}

class MovieTicketReservationTest
    extends Specification
       with Specs2RouteTest
       with Mockito {


  "Movie ticket reservation endpoint" should {

    "Send success code for accepted reservation" in new TestCase {

      reserveSeat returns Reservation.Accepted

      request ~> routes ~> check {
        status shouldEqual StatusCodes.OK
      }
    }

    "Send forbidden code and message for for unknown movie" in new TestCase {

      reserveSeat returns Reservation.NoMovie

      request ~> routes ~> check {
        status shouldEqual StatusCodes.Forbidden

        responseAs[String] shouldEqual "No such movie"
      }
    }

    "Send forbidden code and message for full audience" in new TestCase {

      reserveSeat returns Reservation.NoSeats

      request ~> routes ~> check {
        status shouldEqual StatusCodes.Forbidden

        responseAs[String] shouldEqual "No more available seats"
      }
    }

  }

  trait TestCase extends Scope
    with MovieTicketReservation {

    val movieService = mock[MovieService]

    val request = Post("/movie/ticket", MovieId(imdbId = "tt123",
                                                screenId = "scr_321"))

    def reserveSeat = movieService reserveSeat any()
  }

}