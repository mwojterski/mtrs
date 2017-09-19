package pl.mwt.mtrs.svc

import org.specs2.concurrent.ExecutionEnv
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import pl.mwt.mtrs.data.{MovieId, MovieInfo}
import pl.mwt.mtrs.imdb.MovieTitleService
import pl.mwt.mtrs.shared.FutureUtil.immediateSuccess
import pl.mwt.mtrs.storage.MovieStore

import scala.concurrent.ExecutionContext

class MovieServiceTest
    extends Specification
       with Mockito {

  override def is = s2"""

    Movie service should provide:
      Registration:
        Register movie if title was found         $registerForResolvableTitle
        Reject registration for unknown imdbId    $rejectRegistrationForUnknownImdbId
        Reject registration if movie exists       $rejectRegistrationForExistingMovie

      Reservation:
        Reserve seat for registered movie         $reserveSeatForRegisteredMovie
        Reject reservation for unknown movie      $reserveSeatForUnknownMovie
        Reject reservation if audience is full    $reserveSeatForFullAudienceMovie

      Information:
        Provide data for known movie              $infoForKnownMovie
        Inform of lacking data for unknown movie  $infoForUnknownMovie

  """

  case class Movie(imdbId: String = "test-imdbId",
                   screenId: String = "test-screenId",
                   movieTitle: String = "test-title",
                   availableSeats: Int = 10,
                   reservedSeats: Int = 5)
      extends MovieInfo

  val movie = Movie()
  val title = movie.movieTitle

  implicit val ec = ExecutionContext fromExecutor(_.run())
  implicit val ee = ExecutionEnv fromExecutionContext ec

  def registerForResolvableTitle = {
    val (movieService, movieStore, titleService) = testCase

    movieStore findTitle anyString returns None
    titleService getTitle anyString returns Some(title)

    movieStore add (movie, title) returns true

    movieService register movie should be(Registration.Accepted).await
  }

  def rejectRegistrationForUnknownImdbId = {
    val (movieService, movieStore, titleService) = testCase

    movieStore findTitle anyString returns None
    titleService getTitle anyString returns None

    movieService register movie should be(Registration.InvalidImdbId).await
  }

  def rejectRegistrationForExistingMovie = {
    val (movieService, movieStore, _) = testCase

    movieStore findTitle anyString returns Some(title)
    movieStore add (movie, title) returns false

    movieService register movie should be(Registration.Exists).await
  }

  def reserveSeatForRegisteredMovie = {
    val (movieService, movieStore, _) = testCase

    movieStore findMovie movie returns Some(movie)

    movieStore compareAndSetReservedSeats
      (movie, movie.reservedSeats, movie.reservedSeats + 1) returns true

    movieService reserveSeat movie should be(Reservation.Accepted).await
  }

  def reserveSeatForUnknownMovie = {
    val (movieService, movieStore, _) = testCase

    movieStore findMovie any[MovieId]() returns None

    movieService reserveSeat mock[MovieId] should be(Reservation.NoMovie).await
  }

  def reserveSeatForFullAudienceMovie = {
    val (movieService, movieStore, _) = testCase

    val fullMovie = Movie(reservedSeats = 10)

    movieStore findMovie fullMovie returns Some(fullMovie)

    movieService reserveSeat fullMovie should be(Reservation.NoSeats).await
  }

  def infoForKnownMovie = {
    val (movieService, movieStore, _) = testCase

    movieStore findMovie movie returns Some(movie)

    movieService getInformation movie should beEqualTo(HasInfo(movie) :Info).await
  }

  def infoForUnknownMovie = {
    val (movieService, movieStore, _) = testCase

    movieStore findMovie movie returns None

    movieService getInformation movie should be(NoMovie :Info).await
  }

  def testCase = {

    val movieStore = mock[MovieStore]
    val titleService = mock[MovieTitleService]

    val movieService = new MovieServiceImpl(movieStore, titleService)

    (movieService, movieStore, titleService)
  }
}
