package pl.mwt.mtrs.http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.testkit.Specs2RouteTest
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import pl.mwt.mtrs.http.model.MovieDef
import pl.mwt.mtrs.shared.FutureUtil.immediateSuccess
import pl.mwt.mtrs.svc.{MovieService, Registration}

class MovieRegistrationTest
    extends Specification
       with Specs2RouteTest
       with Mockito {


  "Movie registration endpoint" should {

    "Send created code with location header and message for success" in new TestCase {

      registerMovie returns Registration.Accepted

      request ~> routes ~> check {
        status shouldEqual StatusCodes.Created
        header[Location] should beSome // todo: check generated uri?

        responseAs[String] shouldEqual "Movie registered"
      }
    }

    "Send forbidden code and message for duplicated movie" in new TestCase {

      registerMovie returns Registration.Exists

      request ~> routes ~> check {
        status shouldEqual StatusCodes.Forbidden

        responseAs[String] shouldEqual "Movie already exists"
      }
    }

    "Send bad-request code and message for unrecognized imdb-id" in new TestCase {

      registerMovie returns Registration.InvalidImdbId

      request ~> routes ~> check {
        status shouldEqual StatusCodes.BadRequest

        responseAs[String] shouldEqual "Invalid imdbId"
      }
    }

  }

  trait TestCase extends Scope
    with MovieRegistration {

    val movieService = mock[MovieService]

    val request = Post("/movie", MovieDef(imdbId = "tt123",
                                          screenId = "scr_321",
                                          availableSeats = 25))

    def registerMovie = movieService register any()
  }

}
