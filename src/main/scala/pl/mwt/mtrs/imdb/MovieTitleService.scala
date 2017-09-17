package pl.mwt.mtrs.imdb

import scala.concurrent.Future

trait MovieTitleService {

  def getTitle(imdbId: String): Future[Option[String]]
}
