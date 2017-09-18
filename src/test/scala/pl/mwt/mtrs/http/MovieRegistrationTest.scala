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

    val movieDef = MovieDef(imdbId = "tt123",
                           screenId = "scr_321",
                           availableSeats = 25)

    "Accept and handle proper request" in new TestCase {

      movieService register movieDef returns Registration.Accepted

      Post("/movie", movieDef) ~> routes ~> check {
        status ==== StatusCodes.Created
        header[Location] must beSome // todo: check generated uri?

        responseAs[String] ==== "Movie registered"
      }
    }

    "Reject duplicated movie" in new TestCase {

      movieService register any() returns Registration.Exists

      Post("/movie", movieDef) ~> routes ~> check {
        status ==== StatusCodes.Forbidden

        responseAs[String] ==== "Movie already exists"
      }
    }
  }

  trait TestCase extends Scope
    with MovieRegistration {

    val movieService = mock[MovieService]
  }

}
