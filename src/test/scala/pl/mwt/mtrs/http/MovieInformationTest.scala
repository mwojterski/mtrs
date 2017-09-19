package pl.mwt.mtrs.http

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.Specs2RouteTest
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import pl.mwt.mtrs.http.model.MovieInfo
import pl.mwt.mtrs.shared.FutureUtil.immediateSuccess
import pl.mwt.mtrs.svc.{HasInfo, MovieService, NoMovie}

class MovieInformationTest
    extends Specification
       with Specs2RouteTest
       with Mockito {

  "Movie information endpoint" should {

    "Send ok code and proper payload for known movie" in new TestCase {

      val movieInfo = MovieInfo(imdbId = "tt0111161",
                                screenId = "screen_123456",
                                movieTitle = "The Shawshank Redemption",
                                availableSeats = 100,
                                reservedSeats = 50)

      movieInformation returns HasInfo(movieInfo)

      request ~> routes ~> check {
        status shouldEqual StatusCodes.OK

        contentType shouldEqual ContentTypes.`application/json`
        responseAs[String] shouldEqual
          """{"reservedSeats":50,"screenId":"screen_123456","imdbId":"tt0111161"""" +
          ""","availableSeats":100,"movieTitle":"The Shawshank Redemption"}"""
      }
    }

    "Send client error code and message for unknown movie" in new TestCase {

      movieInformation returns NoMovie

      request ~> routes ~> check {
        status shouldEqual StatusCodes.NotFound

        responseAs[String] shouldEqual "No such movie"
      }
    }

  }

  trait TestCase extends Scope
    with MovieInformation {

    val movieService = mock[MovieService]

    val request = Get("/movie?imdbId=tt0111161&screenId=screen_123456")

    def movieInformation = movieService getInformation any()
  }
}
