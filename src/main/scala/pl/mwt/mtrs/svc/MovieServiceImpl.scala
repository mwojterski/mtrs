package pl.mwt.mtrs.svc

import com.typesafe.scalalogging.StrictLogging
import pl.mwt.mtrs.data.{MovieDef, MovieId, MovieInfo}
import pl.mwt.mtrs.imdb.MovieTitleService
import pl.mwt.mtrs.storage.MovieStore

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

class MovieServiceImpl(movieStore  : MovieStore,
                       titleService: MovieTitleService)
                      (implicit ec: ExecutionContext)
    extends MovieService
       with StrictLogging {

  override def register(movieDef: MovieDef) =
    findTitle(movieDef.imdbId)
      .flatMap {
        case Some(title) =>
          storeMovie(movieDef, title)

        case None =>
          Registration.InvalidImdbId
      }

  override def reserveSeat(movieId: MovieId) =
    movieStore
      .findMovie(movieId)
      .flatMap {
        case Some(movie) =>
          checkAndTryReservingSeat(movie)

        case None =>
          Reservation.NoMovie
      }

  override def getInformation(movieId: MovieId) =
    movieStore
      .findMovie(movieId)
      .map {
        case Some(movie) =>
          HasInfo(movie)

        case None =>
          NoMovie
      }

  private def findTitle(imdbId: String): Future[Option[String]] =
    movieStore
      .findTitle(imdbId)
      .flatMap {
        case None =>
          logger info("Cannot find title for '{}' locally, trying external service", imdbId)
          titleService getTitle imdbId

        case title => title
      }

  private def storeMovie(movieDef: MovieDef, title: String) =
    movieStore
      .add(movieDef, title)
      .map {
        if (_) Registration.Accepted
        else   Registration.Exists
      }

  private def checkAndTryReservingSeat(movie: MovieInfo): Future[Reservation.Value] =
    if (movie.reservedSeats < movie.availableSeats)
      tryReservingSeat(movie)

    else Reservation.NoSeats

  private def tryReservingSeat(movie: MovieInfo) =
    movieStore
      .compareAndSetReservedSeats(movie, movie.reservedSeats, movie.reservedSeats + 1)
      .flatMap {
        if (_) Reservation.Accepted
        else {
          logger info("Lost race, retrying reservation for imdbId='{}', screenId='{}'", movie.imdbId, movie.screenId)
          reserveSeat(movie)
        }
      }

  private implicit def immediateResult[A](a: A): Future[A] =
    Future successful a
}
