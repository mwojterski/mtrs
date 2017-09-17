package pl.mwt.mtrs.storage

import pl.mwt.mtrs.data.{MovieDef, MovieId, MovieInfo}

import scala.concurrent.Future

trait MovieStore {

  def add(movieDef: MovieDef, title: String): Future[Boolean]

  def compareAndSetReservedSeats(movieId: MovieId, currentSeats: Int, newSeats: Int): Future[Boolean]

  def findMovie(movieId: MovieId): Future[Option[MovieInfo]]

  def findTitle(imdbId: String): Future[Option[String]]
}
