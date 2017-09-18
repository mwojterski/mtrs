package pl.mwt.mtrs.http

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.Specs2RouteTest
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import pl.mwt.mtrs.http.model.{MovieId, MovieInfo}
import pl.mwt.mtrs.shared.FutureUtil.immediateSuccess
import pl.mwt.mtrs.svc.{HasInfo, MovieService, NoMovie}

class MovieInformationTest
    extends Specification
       with Specs2RouteTest
       with Mockito {

  "Movie information endpoint" should {

    val request = Get("/movie?imdbId=tt0111161&screenId=screen_123456")

    "Provide correct information for known movie" in new TestCase {

      val movieId = MovieId(imdbId = "tt0111161",
                            screenId = "screen_123456")

      val movieInfo = MovieInfo(imdbId = "tt0111161",
                                screenId = "screen_123456",
                                movieTitle = "The Shawshank Redemption",
                                availableSeats = 100,
                                reservedSeats = 50)

      movieService getInformation movieId returns HasInfo(movieInfo)

      request ~> routes ~> check {
        status ==== StatusCodes.OK
        contentType ==== ContentTypes.`application/json`

        responseAs[String] ====
          """{"reservedSeats":50,"screenId":"screen_123456","imdbId":"tt0111161"""" +
          ""","availableSeats":100,"movieTitle":"The Shawshank Redemption"}"""
      }
    }

    "Respond with error message for unknown movie" in new TestCase {

      movieService getInformation any() returns NoMovie

      request ~> routes ~> check {
        status ==== StatusCodes.NotFound

        responseAs[String] ==== "No such movie"
      }
    }
  }

  trait TestCase extends Scope
    with MovieInformation {

    val movieService = mock[MovieService]
  }
}
