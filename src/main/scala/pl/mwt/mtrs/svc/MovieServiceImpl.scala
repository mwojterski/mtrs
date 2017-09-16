package pl.mwt.mtrs.svc

import pl.mwt.mtrs.data.{MovieDef, MovieId}

import scala.concurrent.Future

object MovieServiceImpl
    extends MovieService {

  override def register(movieDef: MovieDef): Future[Result] =
    Future successful {
      if (movieDef.imdbId == "none") Rejected(s"Sorry, movie $movieDef not found") else Accepted
    }

  override def reserveSeat(movieId: MovieId): Future[Result] =
    Future successful (if (movieId.imdbId == "nope") Rejected("sorry, no more seats") else Accepted)

  override def getInformation(movieId: MovieId): Future[String] =
    Future successful s"Awesome information about $movieId"
}
