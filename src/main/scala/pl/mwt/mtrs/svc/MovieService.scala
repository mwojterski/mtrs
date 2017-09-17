package pl.mwt.mtrs.svc

import pl.mwt.mtrs.data.{MovieDef, MovieId, MovieInfo}

import scala.concurrent.Future

object Registration extends Enumeration {
  val Accepted, InvalidImdbId, Exists = Value
}

object Reservation extends Enumeration {
  val Accepted, NoMovie, NoSeats = Value
}

sealed trait Info
object NoMovie extends Info
final case class HasInfo(info: MovieInfo) extends Info

trait MovieService {

  def register(movieDef: MovieDef): Future[Registration.Value]

  def reserveSeat(movieId: MovieId): Future[Reservation.Value]

  def getInformation(movieId: MovieId): Future[Info]
}
