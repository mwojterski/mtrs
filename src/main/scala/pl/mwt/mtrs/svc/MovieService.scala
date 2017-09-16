package pl.mwt.mtrs.svc

import pl.mwt.mtrs.data.{MovieDef, MovieId}

import scala.concurrent.Future

sealed trait Result
object Accepted extends Result
case class Rejected(msg: String) extends Result

trait MovieService {

  def register(movieDef: MovieDef): Future[Result]

  def reserveSeat(movieId: MovieId): Future[Result]

  def getInformation(movieId: MovieId): Future[String]
}
